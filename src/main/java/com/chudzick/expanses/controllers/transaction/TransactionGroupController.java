package com.chudzick.expanses.controllers.transaction;

import com.chudzick.expanses.beans.TransactionGroupBean;
import com.chudzick.expanses.builders.NotificationMessageListBuilder;
import com.chudzick.expanses.domain.expanses.TransactionGroupDto;
import com.chudzick.expanses.domain.responses.NotificationMessagesBean;
import com.chudzick.expanses.domain.responses.SimpleNotificationMsg;
import com.chudzick.expanses.services.transactions.TransactionGroupService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private TransactionGroupBean transactionGroupBean;

    @Autowired
    private TransactionGroupService transactionGroupService;

    @Autowired
    private NotificationMessagesBean notificationMessagesBean;


    @GetMapping
    public String transactionGroupMain(@ModelAttribute("notifications") List<SimpleNotificationMsg> notifications, Model model) {
        setUpMainPageBeans(notifications);

        model.addAttribute("transactionGroupBean", transactionGroupBean);
        model.addAttribute("notificationMessagesBean", notificationMessagesBean);
        model.addAttribute("transactionGroupDto", new TransactionGroupDto());
        return ""; //TODO RETURN VIEW OF MAIN TRANSACTION GROUP
    }

    @PostMapping(value = "/add")
    public String addNewTransactionGroup(@Valid TransactionGroupDto transactionGroupDto, Model model, BindingResult bindingResult,
                                         RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return ""; //TODO RETURN VIEW OF MAIN TRANSACTION GROUP
        }
        transactionGroupService.addNewTransactionGroup(transactionGroupDto);

        redirectAttributes.addFlashAttribute("notifications", new NotificationMessageListBuilder()
                .withSuccessNotification("{transaction.group.succes.add.message}")
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
}
