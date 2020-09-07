package com.chudzick.expanses.controllers.savings;

import com.chudzick.expanses.beans.responses.NotificationMessagesBean;
import com.chudzick.expanses.beans.savings.SavingGoalBean;
import com.chudzick.expanses.builders.NotificationMessageListBuilder;
import com.chudzick.expanses.domain.paging.PageSettings;
import com.chudzick.expanses.domain.paging.RequestPage;
import com.chudzick.expanses.domain.responses.SimpleNotificationMsg;
import com.chudzick.expanses.domain.savings.SavingGoal;
import com.chudzick.expanses.domain.savings.SavingPayment;
import com.chudzick.expanses.domain.savings.SavingPaymentType;
import com.chudzick.expanses.domain.savings.dto.SavingGoalDto;
import com.chudzick.expanses.domain.savings.dto.SavingPaymentDto;
import com.chudzick.expanses.domain.users.AppUser;
import com.chudzick.expanses.exceptions.InvitationNotFoundException;
import com.chudzick.expanses.factories.paging.PageFactory;
import com.chudzick.expanses.services.savings.SavingGoalService;
import com.chudzick.expanses.services.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "savings")
public class SavingGoalController {
    private static final String NOTIFICATIONS_ATTR_NAME = "notifications";
    private static final String NOTIFICATION_MESSAGES_BEAN_ATTR_NAME = "notificationMessagesBean";
    private static final String REDIRECT_SAVINGS_GOAL_MAIN_PAGE = "redirect:/savings/goal";

    @Autowired
    private SavingGoalService savingGoalService;

    @Autowired
    private UserService userService;

    @Autowired
    private NotificationMessagesBean notificationMessagesBean;


    @GetMapping(value = "goal")
    public String getAllUserSavingGoals(@ModelAttribute(NOTIFICATIONS_ATTR_NAME) List<SimpleNotificationMsg> notifications, Model model) {
        notificationMessagesBean.setNotificationsMessages(notifications);
        return prepareSavingGoalPage(model);
    }


    @PostMapping("goal/add")
    public String addSavingGoal(@ModelAttribute @Valid SavingGoalDto savingGoalDto, BindingResult bindingResult,
                                Model model) {
        if (bindingResult.hasErrors()) {
            notificationMessagesBean.setNotificationsMessages(new NotificationMessageListBuilder()
                    .withFailureNotificationMsg("Błędne dane, błąd podczas zapisu celu")
                    .getNotificationList());
            model.addAttribute("notificationMessagesBean", notificationMessagesBean);
            model.addAttribute("bindingResult", bindingResult);
            return prepareSavingGoalPage(model);
        }
        savingGoalService.addNewGoal(savingGoalDto);
        return REDIRECT_SAVINGS_GOAL_MAIN_PAGE;
    }

    @PostMapping("payment/add/{goalId}")
    public String addSavingPayment(@PathVariable Long goalId, @ModelAttribute @Valid SavingPaymentDto savingPaymentDto,
                                   BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return errorCreatePayment(model, bindingResult);
        }


        SavingGoalBean savingGoalBean = savingGoalService.prepareGoalBean();
        if (savingPaymentDto.getType().equals(SavingPaymentType.ADD)) {
            if (savingGoalBean.getSavingToAllocate().compareTo(savingPaymentDto.getAmount()) < 0) {
                return errorCreatePayment(model, bindingResult);
            }
        } else {
            if (savingPaymentDto.getUserPaymentsSum().compareTo(savingPaymentDto.getAmount()) < 0) {
                return errorCreatePayment(model, bindingResult);
            }
        }

        savingGoalService.addPaymentToGoal(goalId, savingPaymentDto);
        return REDIRECT_SAVINGS_GOAL_MAIN_PAGE;
    }

    @GetMapping("goal/payment/add/all/{goalId}")
    public String paymentAddAll(@PathVariable Long goalId, Model model) {
        SavingGoalBean savingGoalBean = savingGoalService.prepareGoalBean();

        if (savingGoalBean.getSavingToAllocate().compareTo(BigDecimal.ZERO) <= 0) {
            return errorCreatePayment(model, null);
        }

        SavingPaymentDto savingPaymentDto = new SavingPaymentDto();
        savingPaymentDto.setAmount(savingGoalBean.getSavingToAllocate());
        savingPaymentDto.setType(SavingPaymentType.ADD);
        savingGoalService.addPaymentToGoal(goalId, savingPaymentDto);
        return REDIRECT_SAVINGS_GOAL_MAIN_PAGE;
    }

    @GetMapping("goal/payment/take/all/{goalId}")
    public String paymentTakeAll(@PathVariable Long goalId, Model model) {
        SavingGoalBean savingGoalBean = savingGoalService.prepareGoalBean();
        BigDecimal amountToTake = savingGoalBean.getUserSavingGoals().stream()
                .filter(goal -> goal.getSavingGoal().getId().equals(goalId))
                .findFirst()
                .orElseThrow(IllegalStateException::new)
                .getUserPaymentsSum();

        if (amountToTake.compareTo(BigDecimal.ZERO) <= 0) {
            return errorCreatePayment(model, null);
        }

        SavingPaymentDto savingPaymentDto = new SavingPaymentDto();
        savingPaymentDto.setAmount(amountToTake);
        savingPaymentDto.setType(SavingPaymentType.TAKE);
        savingGoalService.addPaymentToGoal(goalId, savingPaymentDto);
        return REDIRECT_SAVINGS_GOAL_MAIN_PAGE;
    }

    @GetMapping("{goalId}/payment/page/{pageNum}")
    public String getPaymentsPage(@PathVariable long goalId, @PathVariable int pageNum, Model model) {
        SavingGoal savingGoal = savingGoalService.finById(goalId);
        RequestPage<SavingPayment> paymentPage = new PageFactory<SavingPayment>()
                .getRequestPage(savingGoal.getSavingPayments(), pageNum, PageSettings.SMALL_PAGE_CONTENT_SIZE.getValue());

        model.addAttribute("paymentPage", paymentPage);
        model.addAttribute("goal", savingGoal);
        return "savings/include/paymentTablePageable";
    }

    @GetMapping("goal/delete/{goalId}")
    public String deleteSavingGoal(@PathVariable Long goalId, Model model, RedirectAttributes redirectAttributes) {
        savingGoalService.deleteGoalById(goalId);
        redirectAttributes.addFlashAttribute(NOTIFICATIONS_ATTR_NAME, new NotificationMessageListBuilder()
                .withSuccessNotification("Pomyślnie usunięto cel oszczędzania")
                .getNotificationList());
        return REDIRECT_SAVINGS_GOAL_MAIN_PAGE;
    }

    @GetMapping("available/users/invite/list/{goalId}")
    public String findAvailableUsersToInvite(@PathVariable long goalId, Model model) {
        List<AppUser> availableUsers = savingGoalService.findAvailableUsers(goalId);
        model.addAttribute("availableUsers", availableUsers);
        model.addAttribute("goalId", goalId);
        return "savings/include/userToInviteList";
    }

    @GetMapping("goal/{goalId}/invite/user/{userId}")
    public String inviteUserToSavingGoal(@PathVariable long goalId, @PathVariable long userId
            , RedirectAttributes redirectAttributes) {
        savingGoalService.inviteUser(goalId, userId);
        redirectAttributes.addFlashAttribute(NOTIFICATIONS_ATTR_NAME, new NotificationMessageListBuilder()
                .withSuccessNotification("Pomyślnie zaproszono użytkownika")
                .getNotificationList());
        return REDIRECT_SAVINGS_GOAL_MAIN_PAGE;
    }

    @GetMapping("invitation/{invitationId}/accept")
    public String acceptInvitation(@PathVariable long invitationId, Model model, RedirectAttributes redirectAttributes) throws InvitationNotFoundException {
        savingGoalService.acceptInvitation(invitationId);
        redirectAttributes.addFlashAttribute(NOTIFICATIONS_ATTR_NAME, new NotificationMessageListBuilder()
                .withSuccessNotification("Poprawnie zaakceptowano zaproszenie")
                .getNotificationList());
        return REDIRECT_SAVINGS_GOAL_MAIN_PAGE;
    }

    @GetMapping("invitation/{invitationId}/reject")
    public String rejectInvitation(@PathVariable long invitationId, Model model, RedirectAttributes redirectAttributes) throws InvitationNotFoundException {
        savingGoalService.rejectInvitation(invitationId);
        redirectAttributes.addFlashAttribute(NOTIFICATIONS_ATTR_NAME, new NotificationMessageListBuilder()
                .withSuccessNotification("Poprawnie odrzucono zaproszenie")
                .getNotificationList());
        return REDIRECT_SAVINGS_GOAL_MAIN_PAGE;
    }

    @ModelAttribute(NOTIFICATIONS_ATTR_NAME)
    public List<SimpleNotificationMsg> defaultAddSuccessValue() {
        return new ArrayList<>();
    }

    private String errorCreatePayment(Model model, BindingResult bindingResult) {
        notificationMessagesBean.setNotificationsMessages(new NotificationMessageListBuilder()
                .withFailureNotificationMsg("Błędne dane, błąd podczas dodania płatności do celu")
                .getNotificationList());
        model.addAttribute(NOTIFICATION_MESSAGES_BEAN_ATTR_NAME, notificationMessagesBean);
        model.addAttribute("bindingResult", bindingResult);
        return prepareSavingGoalPage(model);
    }

    private String prepareSavingGoalPage(Model model) {
        final SavingGoalBean viewBean = savingGoalService.prepareGoalBean();

        model.addAttribute(NOTIFICATION_MESSAGES_BEAN_ATTR_NAME, notificationMessagesBean);
        model.addAttribute("viewBean", viewBean);
        model.addAttribute("savingPaymentType", SavingPaymentType.values());
        model.addAttribute("savingGoalDto", new SavingGoalDto());
        model.addAttribute("savingPaymentDto", new SavingPaymentDto());
        return "savings/savingGoals";
    }

}
