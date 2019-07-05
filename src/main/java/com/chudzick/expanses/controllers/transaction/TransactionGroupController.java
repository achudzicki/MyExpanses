package com.chudzick.expanses.controllers.transaction;

import com.chudzick.expanses.beans.TransactionGroupBean;
import com.chudzick.expanses.builders.NotificationMessageListBuilder;
import com.chudzick.expanses.domain.expanses.TransactionGroupDto;
import com.chudzick.expanses.domain.responses.NotificationMessagesBean;
import com.chudzick.expanses.services.transactions.TransactionGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

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
    public String transactionGroupMain(Model model) {
        setUpMainPageBeans();

        model.addAttribute("transactionGroupBean", transactionGroupBean);
        model.addAttribute("transactionGroupDto", new TransactionGroupDto());
        return ""; //TODO RETURN VIEW OF MAIN TRANSACTION GROUP
    }

    @PostMapping
    public String addNewTransactionGroup(@Valid TransactionGroupDto transactionGroupDto, Model model, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ""; //TODO RETURN VIEW OF MAIN TRANSACTION GROUP
        }

        setUpMainPageBeans();
        transactionGroupService.addNewTransactionGroup(transactionGroupDto);

        notificationMessagesBean.setNotificationsMessages(
                new NotificationMessageListBuilder()
                        .withSuccessNotification("{transaction.group.succes.add.message}")
                        .getNotificationList()
        );

        model.addAttribute("transactionGroupBean", transactionGroupBean);
        model.addAttribute("notificationMessagesBean", notificationMessagesBean);
        model.addAttribute("transactionGroupDto", new TransactionGroupDto());
        return ""; //TODO RETURN VIEW OF MAIN TRANSACTION GROUP
    }

    private void setUpMainPageBeans() {
        transactionGroupBean.setUsersTransactionsGroups(transactionGroupService.getAllGroups());
    }
}
