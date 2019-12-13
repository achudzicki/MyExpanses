package com.chudzick.expanses.controllers.transaction;

import com.chudzick.expanses.beans.responses.NotificationMessagesBean;
import com.chudzick.expanses.beans.transactions.TransactionGroupBean;
import com.chudzick.expanses.beans.transactions.TransactionGroupUsageBean;
import com.chudzick.expanses.builders.NotificationMessageListBuilder;
import com.chudzick.expanses.domain.expanses.ConstantTransaction;
import com.chudzick.expanses.domain.expanses.SingleTransaction;
import com.chudzick.expanses.domain.expanses.TransactionGroup;
import com.chudzick.expanses.domain.expanses.dto.ConstantTransactionDto;
import com.chudzick.expanses.domain.expanses.dto.SingleTransactionDto;
import com.chudzick.expanses.domain.expanses.dto.TransactionGroupDto;
import com.chudzick.expanses.domain.responses.SimpleNotificationMsg;
import com.chudzick.expanses.exceptions.UserNotPermittedToActionException;
import com.chudzick.expanses.services.permissions.PermissionsService;
import com.chudzick.expanses.services.transactions.ConstantTransactionService;
import com.chudzick.expanses.services.transactions.SingleTransactionService;
import com.chudzick.expanses.services.transactions.TransactionGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "transaction/group")
public class TransactionGroupController {
    private final String SUCCESS_NOTIFICATION_MESSAGE = "Poprawnie dodano nową grupę";
    private static final String NOTIFICATIONS_ATTR_NAME = "notifications";

    @Autowired
    private TransactionGroupBean transactionGroupBean;

    @Autowired
    private TransactionGroupService transactionGroupService;

    @Autowired
    private NotificationMessagesBean notificationMessagesBean;

    @Autowired
    private PermissionsService permissionsService;

    @Autowired
    private SingleTransactionService<SingleTransaction, SingleTransactionDto> singleTransactionService;

    @Autowired
    private ConstantTransactionService<ConstantTransaction, ConstantTransactionDto> constantTransactionService;

    @Autowired
    private TransactionGroupUsageBean transactionGroupUsageBean;


    @GetMapping
    public String transactionGroupMain(@ModelAttribute(NOTIFICATIONS_ATTR_NAME) List<SimpleNotificationMsg> notifications, Model model) {
        setUpMainPageBeans(notifications);
        addBasicModelAttributes(model);

        return "transaction/transactionGroupMain";
    }

    @PostMapping(value = "/add")
    public String addNewTransactionGroup(@Valid @ModelAttribute TransactionGroupDto transactionGroupDto, BindingResult bindingResult, Model model,
                                         RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            setUpMainPageBeans(new NotificationMessageListBuilder()
                    .withFailureNotificationMsg("Wystąpił problem podczas dodawania nowej grupy")
                    .getNotificationList());

            addBasicModelAttributes(model);
            model.addAttribute("bindingResult", bindingResult);
            return "transaction/transactionGroupMain";
        }

        transactionGroupService.addNewTransactionGroup(transactionGroupDto);

        redirectAttributes.addFlashAttribute(NOTIFICATIONS_ATTR_NAME, new NotificationMessageListBuilder()
                .withSuccessNotification(SUCCESS_NOTIFICATION_MESSAGE)
                .getNotificationList());
        return "redirect:/transaction/group";
    }

    @PostMapping(value = "/delete/{groupId}")
    public String deleteTransactionGroup(@PathVariable long groupId, RedirectAttributes redirectAttributes) throws UserNotPermittedToActionException {
        permissionsService.checkUserPermissionsToDeleteGroup(groupId);


        int transactionsToGroup = singleTransactionService.countTransactionsByGroup(groupId) + constantTransactionService.countTransactionsByGroup(groupId);

        if (transactionsToGroup > 0) {
            redirectAttributes.addFlashAttribute(NOTIFICATIONS_ATTR_NAME, new NotificationMessageListBuilder()
                    .withFailureNotificationMsg("Usuwanie grupy nie powiodło się.\nLiczba znalezionych powiązań : " + transactionsToGroup)
                    .getNotificationList());
            return "redirect:/transaction/group/usage/" + groupId;
        }

        transactionGroupService.deleteTransactionGroup(groupId);
        redirectAttributes.addFlashAttribute(NOTIFICATIONS_ATTR_NAME, new NotificationMessageListBuilder()
                .withSuccessNotification("Poprawnie usunięto grupę")
                .getNotificationList());

        return "redirect:/transaction/group";
    }

    @GetMapping(value = "/usage/{groupId}")
    public String viewTransactionsToSelectedGroup(@ModelAttribute(NOTIFICATIONS_ATTR_NAME) List<SimpleNotificationMsg> notifications, Model model
            , @PathVariable int groupId) {

        final List<SingleTransaction> groupTransactions = singleTransactionService.findAllTransactionsByGroupId(groupId);
        final List<ConstantTransaction> constantTransactions = constantTransactionService.findAllTransactionsByGroupId(groupId);
        final TransactionGroup transactionGroup = transactionGroupService.findById(groupId);
        transactionGroupUsageBean.setGroupSingleTransactions(groupTransactions);
        transactionGroupUsageBean.setTransactionGroup(transactionGroup);
        transactionGroupUsageBean.setConstantTransactions(constantTransactions);
        notificationMessagesBean.setNotificationsMessages(notifications);
        model.addAttribute("transactionGroupUsageBean", transactionGroupUsageBean);
        model.addAttribute("notificationMessagesBean", notificationMessagesBean);
        return "transaction/TransactionGroupUsageTable";
    }

    @ModelAttribute(name = NOTIFICATIONS_ATTR_NAME)
    public List<SimpleNotificationMsg> defaultNotificationList() {
        return new ArrayList<>();
    }

    private void setUpMainPageBeans(List<SimpleNotificationMsg> notifications) {
        notificationMessagesBean.setNotificationsMessages(notifications);
        transactionGroupBean.setUsersTransactionsGroups(transactionGroupService.getAllGroups());
    }

    private void addBasicModelAttributes(Model model) {
        model.addAttribute("notificationMessagesBean", notificationMessagesBean);
        model.addAttribute("transactionGroupBean", transactionGroupBean);
        model.addAttribute("transactionGroupDto", new TransactionGroupDto());
    }
}
