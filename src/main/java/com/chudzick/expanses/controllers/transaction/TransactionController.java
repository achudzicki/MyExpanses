package com.chudzick.expanses.controllers.transaction;

import com.chudzick.expanses.beans.responses.NotificationMessagesBean;
import com.chudzick.expanses.builders.NotificationMessageListBuilder;
import com.chudzick.expanses.domain.expanses.*;
import com.chudzick.expanses.domain.informations.CycleInformation;
import com.chudzick.expanses.domain.responses.SimpleNotificationMsg;
import com.chudzick.expanses.domain.statictics.ActualTransactionStats;
import com.chudzick.expanses.exceptions.NoActiveCycleException;
import com.chudzick.expanses.exceptions.UserNotPermittedToActionException;
import com.chudzick.expanses.factories.ActualTransactionStatsFactory;
import com.chudzick.expanses.services.transactions.CycleService;
import com.chudzick.expanses.services.transactions.TransactionGroupService;
import com.chudzick.expanses.services.transactions.UserTransactionService;
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
import java.util.Optional;

@Controller
@RequestMapping(value = "transaction")
public class TransactionController {
    private static final Logger LOG = LoggerFactory.getLogger(TransactionController.class);
    private static final int NUMBERS_OF_TRANSACTIONS_DISPLAY = 5;
    private static final String NOTIFICATIONS_ATTR_NAME = "notifications";
    private static final String NOTIFICATION_MESSAGES_BEAN_NAME = "notificationMessagesBean";
    private static final String TRANSACTION_ADD_ERROR_MSG = "Wystąpił problem podczas dodawania nowej transakcji";

    @Autowired
    private TransactionGroupService transactionGroupService;

    @Autowired
    private UserTransactionService userTransactionService;

    @Autowired
    private NotificationMessagesBean notificationMessagesBean;

    @Autowired
    private CycleService cycleService;

    @GetMapping(value = "")
    public String getTransactionPage(@ModelAttribute(NOTIFICATIONS_ATTR_NAME) List<SimpleNotificationMsg> notifications,
                                     Model model) {
        initBasicAddTransactionModelAttributes(model);
        notificationMessagesBean.setNotificationsMessages(notifications);


        model.addAttribute(NOTIFICATION_MESSAGES_BEAN_NAME, notificationMessagesBean);
        return "transaction/addNewTransaction";
    }

    @GetMapping(value = "/constant")
    public String getConstantTransactionsPAge(@ModelAttribute(NOTIFICATIONS_ATTR_NAME) List<SimpleNotificationMsg> notifications, Model model) {

        initBasicAddConstantTransactionModelAttributes(model);
        notificationMessagesBean.setNotificationsMessages(notifications);

        model.addAttribute(NOTIFICATION_MESSAGES_BEAN_NAME, notificationMessagesBean);
        return "transaction/addNewConstantTransaction";
    }

    @PostMapping(value = "/add")
    public String addNewTransaction(@ModelAttribute("singleTransactionDto") @Valid SingleTransactionDto singleTransactionDto, BindingResult bindingResult,
                                    RedirectAttributes redirectAttributes, Model model) throws NoActiveCycleException {
        LOG.info("Try to add new Transaction");

        if (bindingResult.hasErrors()) {
            initBasicAddTransactionModelAttributes(model);
            notificationMessagesBean.setNotificationsMessages(new NotificationMessageListBuilder()
                    .withFailureNotificationMsg(TRANSACTION_ADD_ERROR_MSG)
                    .getNotificationList());
            model.addAttribute(NOTIFICATION_MESSAGES_BEAN_NAME, notificationMessagesBean);
            LOG.warn("Problem with adding new transaction, bindingResult Errors");
            return "transaction/addNewTransaction";
        }

        userTransactionService.addNewSingleTransaction(singleTransactionDto);

        List<SimpleNotificationMsg> successNotification = new NotificationMessageListBuilder()
                .withSuccessNotification("Dodano nową transakcję")
                .getNotificationList();

        LOG.info("New transaction successfully added");
        redirectAttributes.addFlashAttribute(NOTIFICATIONS_ATTR_NAME, successNotification);
        return "redirect:/transaction";
    }

    @PostMapping(value = "/constant/add")
    public String addConstantTransaction(@ModelAttribute("constantTransactionDto") @Valid ConstantTransactionDto constantTransactionDto, BindingResult bindingResult,
                                         RedirectAttributes redirectAttributes, Model model) throws NoActiveCycleException {
        LOG.info("try to add new constant transaction");
        if (bindingResult.hasErrors()) {
            LOG.warn("Invalid form passed, binding error");
            initBasicAddConstantTransactionModelAttributes(model);
            notificationMessagesBean.setNotificationsMessages(new NotificationMessageListBuilder()
                    .withFailureNotificationMsg(TRANSACTION_ADD_ERROR_MSG)
                    .getNotificationList());
            model.addAttribute(NOTIFICATION_MESSAGES_BEAN_NAME, notificationMessagesBean);
            return "transaction/addNewConstantTransaction";
        }
        userTransactionService.addNewConstantTransaction(constantTransactionDto);
        LOG.info("New constant transaction successfully added");
        return "redirect:/transaction/constant";
    }

    @GetMapping(value = "/all")
    public String viewAllTransactions(@ModelAttribute(NOTIFICATIONS_ATTR_NAME) List<SimpleNotificationMsg> notifications, Model model) throws NoActiveCycleException {
        List<UserTransactions> allTransactionsPerCycle = userTransactionService.findAll();
        ActualTransactionStats actualTransactionStats = new ActualTransactionStatsFactory().fromTransactionList(allTransactionsPerCycle);
        Optional<Cycle> activeCycle = cycleService.findActiveCycle();

        notificationMessagesBean.setNotificationsMessages(notifications);
        activeCycle.ifPresent(cycle -> model.addAttribute("cycleDisplayInfo", CycleInformation.fromCycle(cycle)));
        model.addAttribute(NOTIFICATION_MESSAGES_BEAN_NAME, notificationMessagesBean);
        model.addAttribute("transactionsList", allTransactionsPerCycle);
        model.addAttribute("actualTransactionStats", actualTransactionStats);
        return "transaction/allCycleTransactions";
    }

    @PostMapping(value = "/delete/{transactionId}")
    public String deleteTransaction(@PathVariable long transactionId, HttpServletRequest request,
                                    RedirectAttributes redirectAttributes) throws UserNotPermittedToActionException {
        boolean success = userTransactionService.deleteSingleTransactionById(transactionId);
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
        List<SingleTransaction> lastFiveTransactions = userTransactionService.findLastSingleTransactionsLimitBy(NUMBERS_OF_TRANSACTIONS_DISPLAY);
        initConstantAndSingleTransactionBasicModel(model);

        model.addAttribute("lastTransactions", lastFiveTransactions);
        model.addAttribute("singleTransactionDto", new SingleTransactionDto());
    }

    private void initBasicAddConstantTransactionModelAttributes(Model model) {
        List<ConstantTransaction> constantTransactions = userTransactionService.findAllActiveConstantTransactions();
        initConstantAndSingleTransactionBasicModel(model);

        model.addAttribute("constantTransactions", constantTransactions);
        model.addAttribute("constantTransactionDto", new ConstantTransactionDto());
    }

    private void initConstantAndSingleTransactionBasicModel(Model model) {
        List<TransactionGroup> transactionGroups = transactionGroupService.getAllGroups();
        Optional<Cycle> cycle = cycleService.findActiveCycle();

        cycle.ifPresent(activeCycle -> {
            model.addAttribute("activeCycle", activeCycle);
            model.addAttribute("cycleDisplayInfo", CycleInformation.fromCycle(activeCycle));
        });
        model.addAttribute("transactionGroups", transactionGroups);
        model.addAttribute("transactionTypes", TransactionType.values());
    }
}
