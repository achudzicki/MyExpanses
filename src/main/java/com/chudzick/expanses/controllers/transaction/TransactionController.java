package com.chudzick.expanses.controllers.transaction;

import com.chudzick.expanses.builders.NotificationMessageListBuilder;
import com.chudzick.expanses.domain.expanses.SingleTransactionDto;
import com.chudzick.expanses.domain.expanses.TransactionGroup;
import com.chudzick.expanses.domain.expanses.TransactionType;
import com.chudzick.expanses.domain.responses.NotificationMessagesBean;
import com.chudzick.expanses.repositories.TransactionGroupRepository;
import com.chudzick.expanses.services.transactions.SingleTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping(value = "transaction")
public class TransactionController {

    @Autowired
    private TransactionGroupRepository transactionGroupRepository;

    @Autowired
    private SingleTransactionService singleTransactionService;

    @Autowired
    private NotificationMessagesBean notificationMessagesBean;

    @GetMapping(value = "")
    public String getTransactionPage(Model model) {

        initBasicAddTransactionModelAttributes(model);
        return "transaction/transactionMain";
    }

    @PostMapping(value = "/add")
    public String addNewTransaction(@ModelAttribute SingleTransactionDto singleTransactionDto, Model model) {
        singleTransactionService.addNewTransaction(singleTransactionDto);

        notificationMessagesBean.setNotificationsMessages(
                new NotificationMessageListBuilder()
                        .withSuccessNotification("New transaction added")
                        .getNotificationList()
        );

        initBasicAddTransactionModelAttributes(model);
        model.addAttribute("notificationMessagesBean", notificationMessagesBean);
        return "transaction/transactionMain";
    }

    private void initBasicAddTransactionModelAttributes(Model model) {
        List<TransactionGroup> transactionGroups = transactionGroupRepository.findAll();
        model.addAttribute("transactionGroups", transactionGroups);
        model.addAttribute("singleTransactionDto", new SingleTransactionDto());
        model.addAttribute("transactionTypes", TransactionType.values());
    }
}
