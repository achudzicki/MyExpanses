package com.chudzick.expanses.controllers.transaction;

import com.chudzick.expanses.builders.NotificationMessageListBuilder;
import com.chudzick.expanses.domain.expanses.SingleTransaction;
import com.chudzick.expanses.domain.expanses.SingleTransactionDto;
import com.chudzick.expanses.domain.expanses.TransactionGroup;
import com.chudzick.expanses.domain.expanses.TransactionType;
import com.chudzick.expanses.domain.responses.NotificationMessagesBean;
import com.chudzick.expanses.domain.responses.SimpleNotificationMsg;
import com.chudzick.expanses.repositories.TransactionGroupRepository;
import com.chudzick.expanses.services.transactions.SingleTransactionService;
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
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping(value = "transaction")
public class TransactionController {
    private static final int NUMBERS_OF_TRANSACTIONS_DISPLAY = 5;
    private static final String NOTIFICATIONS_ATTR_NAME = "notifications";

    @Autowired
    private TransactionGroupRepository transactionGroupRepository;

    @Autowired
    private SingleTransactionService singleTransactionService;

    @Autowired
    private NotificationMessagesBean notificationMessagesBean;

    @GetMapping(value = "")
    public String getTransactionPage(@ModelAttribute(NOTIFICATIONS_ATTR_NAME) List<SimpleNotificationMsg> notifications,
                                     Model model) {

        initBasicAddTransactionModelAttributes(model);
        notificationMessagesBean.setNotificationsMessages(notifications);

        model.addAttribute("notificationMessagesBean", notificationMessagesBean);
        return "transaction/addNewTransaction";
    }

    @PostMapping(value = "/add")
    public String addNewTransaction(@ModelAttribute("singleTransactionDto") @Valid  SingleTransactionDto singleTransactionDto, BindingResult bindingResult,
                                    RedirectAttributes redirectAttributes, Model model) {
        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors().forEach(err -> System.out.println(err.getDefaultMessage()));
            initBasicAddTransactionModelAttributes(model);
            return "transaction/addNewTransaction";
        }

        singleTransactionService.addNewTransaction(singleTransactionDto);

        List<SimpleNotificationMsg> successNotification = new NotificationMessageListBuilder()
                .withSuccessNotification("Dodano nową transakcję")
                .getNotificationList();

        redirectAttributes.addFlashAttribute(NOTIFICATIONS_ATTR_NAME, successNotification);
        return "redirect:/transaction";
    }

    @ModelAttribute(NOTIFICATIONS_ATTR_NAME)
    public List<SimpleNotificationMsg> defaultAddSuccessValue() {
        return new ArrayList<>();
    }

    private void initBasicAddTransactionModelAttributes(Model model) {
        List<SingleTransaction> lastFiveTransactions = singleTransactionService.findLastSingleTransactionsLimitBy(NUMBERS_OF_TRANSACTIONS_DISPLAY);
        //TODO zmienić na serwis
        List<TransactionGroup> transactionGroups = transactionGroupRepository.findAll();
        model.addAttribute("transactionGroups", transactionGroups);
        model.addAttribute("lastTransactions", lastFiveTransactions);
        model.addAttribute("singleTransactionDto", new SingleTransactionDto());
        model.addAttribute("transactionTypes", TransactionType.values());
    }
}
