package com.chudzick.expanses.controllers.transaction;

import com.chudzick.expanses.beans.TransactionGroupBean;
import com.chudzick.expanses.builders.NotificationMessageListBuilder;
import com.chudzick.expanses.domain.expanses.TransactionGroupDto;
import com.chudzick.expanses.domain.responses.NotificationMessagesBean;
import com.chudzick.expanses.domain.responses.SimpleNotificationMsg;
import com.chudzick.expanses.services.transactions.TransactionGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "transaction/group")
public class TransactionGroupController {
    private final String SUCCESS_NOTIFICATION_MESSAGE = "Poprawnie dodano nową grupę" ;

    @Autowired
    private TransactionGroupBean transactionGroupBean;

    @Autowired
    private TransactionGroupService transactionGroupService;

    @Autowired
    private NotificationMessagesBean notificationMessagesBean;


    @GetMapping
    public String transactionGroupMain(@ModelAttribute("notifications") List<SimpleNotificationMsg> notifications, Model model) {
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

        redirectAttributes.addFlashAttribute("notifications", new NotificationMessageListBuilder()
                .withSuccessNotification(SUCCESS_NOTIFICATION_MESSAGE)
                .getNotificationList());
        return "redirect:/transaction/group";
    }

    @ModelAttribute(name = "notifications")
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
