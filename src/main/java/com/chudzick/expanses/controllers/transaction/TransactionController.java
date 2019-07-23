package com.chudzick.expanses.controllers.transaction;

import com.chudzick.expanses.beans.responses.NotificationMessagesBean;
import com.chudzick.expanses.builders.NotificationMessageListBuilder;
import com.chudzick.expanses.domain.expanses.SingleTransaction;
import com.chudzick.expanses.domain.expanses.SingleTransactionDto;
import com.chudzick.expanses.domain.expanses.TransactionGroup;
import com.chudzick.expanses.domain.expanses.TransactionType;
import com.chudzick.expanses.domain.responses.SimpleNotificationMsg;
import com.chudzick.expanses.domain.statictics.ActualTransactionStats;
import com.chudzick.expanses.exceptions.UserNotPermittedToActionException;
import com.chudzick.expanses.factories.ActualTransactionStatsFactory;
import com.chudzick.expanses.services.transactions.SingleTransactionService;
import com.chudzick.expanses.services.transactions.TransactionGroupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "transaction")
public class TransactionController {
    private static final Logger LOG = LoggerFactory.getLogger(TransactionController.class);
    private static final int NUMBERS_OF_TRANSACTIONS_DISPLAY = 5;
    private static final String NOTIFICATIONS_ATTR_NAME = "notifications";

    @Autowired
    private TransactionGroupService transactionGroupService;

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
    public String addNewTransaction(@ModelAttribute("singleTransactionDto") @Valid SingleTransactionDto singleTransactionDto, BindingResult bindingResult,
                                    RedirectAttributes redirectAttributes, Model model) {
        LOG.info("Try to add new Transaction");

        if (bindingResult.hasErrors()) {
            initBasicAddTransactionModelAttributes(model);
            notificationMessagesBean.setNotificationsMessages(new NotificationMessageListBuilder()
                    .withFailureNotificationMsg("Wystąpił problem podczas dodawania nowej transakcji")
                    .getNotificationList());
            model.addAttribute("notificationMessagesBean", notificationMessagesBean);
            LOG.warn("Problem with adding new transaction, bindingResult Errors");
            return "transaction/addNewTransaction";
        }

        singleTransactionService.addNewTransaction(singleTransactionDto);

        List<SimpleNotificationMsg> successNotification = new NotificationMessageListBuilder()
                .withSuccessNotification("Dodano nową transakcję")
                .getNotificationList();

        LOG.info("New successfully transaction added");
        redirectAttributes.addFlashAttribute(NOTIFICATIONS_ATTR_NAME, successNotification);
        return "redirect:/transaction";
    }

    @GetMapping(value = "/all")
    public String viewAllTransactions(@ModelAttribute(NOTIFICATIONS_ATTR_NAME) List<SimpleNotificationMsg> notifications, Model model) {
        List<SingleTransaction> allTransactionsPerCycle = singleTransactionService.findAll();
        ActualTransactionStats actualTransactionStats = new ActualTransactionStatsFactory().fromTransactionList(allTransactionsPerCycle);

        model.addAttribute("transactionsList", allTransactionsPerCycle);
        model.addAttribute("actualTransactionStats", actualTransactionStats);
        return "transaction/allCycleTransactions";
    }

    @PostMapping(value = "/delete/{transactionId}")
    public String deleteTransaction(@PathVariable long transactionId, HttpServletRequest request,
                                    RedirectAttributes redirectAttributes) throws UserNotPermittedToActionException {
        boolean success = singleTransactionService.deleteTransactionById(transactionId);
        String referrerUrl = request.getHeader(HttpHeaders.REFERER);

        if (!success) {
            redirectAttributes.addFlashAttribute(NOTIFICATIONS_ATTR_NAME, new NotificationMessageListBuilder()
                    .withFailureNotificationMsg("Wystąpił błąd podczas usuwania transakcji")
                    .getNotificationList());
        } else {
            redirectAttributes.addFlashAttribute(NOTIFICATIONS_ATTR_NAME, new NotificationMessageListBuilder()
                    .withSuccessNotification("Płatność usunięta poprawnie")
                    .getNotificationList());
        }

        return "redirect:" + referrerUrl;
    }

    @ModelAttribute(NOTIFICATIONS_ATTR_NAME)
    public List<SimpleNotificationMsg> defaultAddSuccessValue() {
        return new ArrayList<>();
    }

    private void initBasicAddTransactionModelAttributes(Model model) {
        List<SingleTransaction> lastFiveTransactions = singleTransactionService.findLastSingleTransactionsLimitBy(NUMBERS_OF_TRANSACTIONS_DISPLAY);

        List<TransactionGroup> transactionGroups = transactionGroupService.getAllGroups();
        model.addAttribute("transactionGroups", transactionGroups);
        model.addAttribute("lastTransactions", lastFiveTransactions);
        model.addAttribute("singleTransactionDto", new SingleTransactionDto());
        model.addAttribute("transactionTypes", TransactionType.values());
    }
}
