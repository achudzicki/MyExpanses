package com.chudzick.expanses.controllers.transaction;

import com.chudzick.expanses.beans.responses.NotificationMessagesBean;
import com.chudzick.expanses.beans.transactions.DeleteConstantTransactionBean;
import com.chudzick.expanses.builders.NotificationMessageListBuilder;
import com.chudzick.expanses.domain.ApplicationActions;
import com.chudzick.expanses.domain.expanses.*;
import com.chudzick.expanses.domain.expanses.dto.ConstantTransactionDto;
import com.chudzick.expanses.domain.expanses.dto.SingleTransactionDto;
import com.chudzick.expanses.domain.filter.SearchCriteria;
import com.chudzick.expanses.domain.filter.SearchOperation;
import com.chudzick.expanses.domain.filter.transactions.TransactionFilterRequest;
import com.chudzick.expanses.domain.filter.transactions.TransactionSpecificationBuilder;
import com.chudzick.expanses.domain.informations.CycleInformation;
import com.chudzick.expanses.domain.paging.PageSettings;
import com.chudzick.expanses.domain.paging.RequestPage;
import com.chudzick.expanses.domain.responses.SimpleNotificationMsg;
import com.chudzick.expanses.domain.statictics.ActualTransactionStats;
import com.chudzick.expanses.exceptions.AppObjectNotFoundException;
import com.chudzick.expanses.exceptions.NoActiveCycleException;
import com.chudzick.expanses.exceptions.UserNotPermittedToActionException;
import com.chudzick.expanses.factories.ActualTransactionStatsFactory;
import com.chudzick.expanses.factories.paging.PageFactory;
import com.chudzick.expanses.services.transactions.ConstantTransactionService;
import com.chudzick.expanses.services.transactions.CycleService;
import com.chudzick.expanses.services.transactions.SingleTransactionService;
import com.chudzick.expanses.services.transactions.TransactionGroupService;
import com.chudzick.expanses.util.ListsUnion;
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
    private SingleTransactionService<SingleTransaction, SingleTransactionDto> singleTransactionService;

    @Autowired
    private ConstantTransactionService<ConstantTransaction, ConstantTransactionDto> constantTransactionService;

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

        singleTransactionService.addNewTransaction(singleTransactionDto);

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
        constantTransactionService.addNewTransaction(constantTransactionDto);
        LOG.info("New constant transaction successfully added");
        return "redirect:/transaction/constant";
    }

    @GetMapping(value = "/all")
    public String viewAllTransactions(@ModelAttribute(NOTIFICATIONS_ATTR_NAME) List<SimpleNotificationMsg> notifications, Model model) {
        List<SingleTransaction> allSingleTransactions = singleTransactionService.findAll();
        List<ConstantTransaction> allConstantTransactions = constantTransactionService.findAll();
        List<UserTransactions> allTransactionsPerCycle = ListsUnion.union(allConstantTransactions, allSingleTransactions);
        Optional<Cycle> activeCycle = cycleService.findActiveCycle();
        ActualTransactionStats actualTransactionStats = new ActualTransactionStatsFactory().fromTransactionList(allTransactionsPerCycle, activeCycle);
        RequestPage<SingleTransaction> transactionPage = new PageFactory<SingleTransaction>().getRequestPage(allSingleTransactions, PageSettings.FIRST_PAGE.getValue(), PageSettings.PAGE_CONTENT_SIZE.getValue());

        notificationMessagesBean.setNotificationsMessages(notifications);
        activeCycle.ifPresent(cycle -> {
            model.addAttribute("cycleDisplayInfo", CycleInformation.fromCycle(cycle));
            model.addAttribute("saveGoal", activeCycle.get().getSaveGoal());
        });
        model.addAttribute(NOTIFICATION_MESSAGES_BEAN_NAME, notificationMessagesBean);
        model.addAttribute("transactionPage", transactionPage);
        model.addAttribute("constantTransactions", allConstantTransactions);
        model.addAttribute("actualTransactionStats", actualTransactionStats);
        model.addAttribute("transactionTypes", TransactionType.values());
        model.addAttribute("transactionGroups", transactionGroupService.getAllGroups());
        model.addAttribute("transactionFilterRequest", new TransactionFilterRequest());
        return "transaction/allCycleTransactions";
    }

    @PostMapping(value = "all/filter")
    public String filterTransaction(@ModelAttribute TransactionFilterRequest transactionFilterRequest, Model model) {

        //TODO to do jakiej factory
        List<SearchCriteria> criteriaList = new ArrayList<>();
        if (transactionFilterRequest.getDateFrom() != null) {
            criteriaList.add(new SearchCriteria("transactionDate", SearchOperation.GRATER_THAN, transactionFilterRequest.getDateFrom()));
        }
        if (transactionFilterRequest.getDateTo() != null) {
            criteriaList.add(new SearchCriteria("transactionDate", SearchOperation.LESS_THAN, transactionFilterRequest.getDateFrom()));
        }
        if (transactionFilterRequest.getTransactionGroup() != null) {
            criteriaList.add(new SearchCriteria("transactionGroup", SearchOperation.EQUALITY, transactionGroupService.findById(transactionFilterRequest.getTransactionGroup())));
        }
        if (transactionFilterRequest.getTransactionType() != null) {
            criteriaList.add(new SearchCriteria("transactionType", SearchOperation.EQUALITY, transactionFilterRequest.getTransactionType()));
        }

        TransactionSpecificationBuilder<SingleTransaction> builder = new TransactionSpecificationBuilder<>(criteriaList);
        List<SingleTransaction> filtred = singleTransactionService.findFilteredTransactions(builder.build());
        System.out.println();
        int i = 0;
        return "";
    }

    @GetMapping(value = "single/page/{pageNumber}")
    public String getSingleTransactionPage(@PathVariable int pageNumber, Model model) {
        List<SingleTransaction> allSingleTransactions = singleTransactionService.findAll();
        RequestPage<SingleTransaction> transactionPage = new PageFactory<SingleTransaction>().getRequestPage(allSingleTransactions, pageNumber, PageSettings.PAGE_CONTENT_SIZE.getValue());

        model.addAttribute("transactionPage", transactionPage);
        return "transaction/include/singleTransactionPageableTable";
    }

    @PostMapping(value = "/delete/{transactionId}")
    public String deleteTransaction(@PathVariable long transactionId, @ModelAttribute(name = "transactionDuration") TransactionDuration transactionDuration, HttpServletRequest request,
                                    RedirectAttributes redirectAttributes) throws UserNotPermittedToActionException, AppObjectNotFoundException {
        boolean success;
        if (transactionDuration.equals(TransactionDuration.CONSTANT)) {
            return "redirect:/transaction/constant/delete/" + transactionId;
        } else {
            success = singleTransactionService.deleteTransactionById(transactionId);
        }

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

    @GetMapping(value = "/constant/delete/{id}")
    public String constantTransactionDelete(@PathVariable long id, Model model,
                                            RedirectAttributes redirectAttributes) throws AppObjectNotFoundException, UserNotPermittedToActionException {
        ConstantTransaction transaction = constantTransactionService.findTransactionById(id)
                .orElseThrow(() -> new AppObjectNotFoundException(ApplicationActions.DELETE_CONSTANT_TRANSACTION, "Nie znaleziono transakcji do usunięcia"));

        if (transaction.getCycles().isEmpty() || transaction.getCycles().size() == 1) {
            constantTransactionService.deleteTransactionById(id);
            redirectAttributes.addFlashAttribute(NOTIFICATIONS_ATTR_NAME, new NotificationMessageListBuilder()
                    .withSuccessNotification("Transakcja stała poprawnie usunięta")
                    .getNotificationList());
            return "redirect:/transaction/all";
        }

        model.addAttribute("transactionToDelete", transaction);
        model.addAttribute("cycles", transaction.getCycles());
        return "transaction/deleteConstantTransaction";
    }

    @PostMapping(value = "/constant/delete")
    public String constantTransactionDeletePost(@ModelAttribute("deleteConstantTransactionBean") DeleteConstantTransactionBean deleteBean,
                                                RedirectAttributes redirectAttributes) throws AppObjectNotFoundException, UserNotPermittedToActionException {
        constantTransactionService.deleteTransactionsFromCycles(deleteBean.getConstantTransactionId(), deleteBean.getConstantTransDto());

        redirectAttributes.addFlashAttribute(NOTIFICATIONS_ATTR_NAME, new NotificationMessageListBuilder()
                .withSuccessNotification("Transakcja stała poprawnie usunięta")
                .getNotificationList());
        return "redirect:/transaction/all";
    }

    @ModelAttribute(NOTIFICATIONS_ATTR_NAME)
    public List<SimpleNotificationMsg> defaultAddSuccessValue() {
        return new ArrayList<>();
    }

    private void initBasicAddTransactionModelAttributes(Model model) {
        List<SingleTransaction> lastFiveTransactions = singleTransactionService.findLastSingleTransactionsLimitBy(NUMBERS_OF_TRANSACTIONS_DISPLAY);
        initConstantAndSingleTransactionBasicModel(model);

        model.addAttribute("lastTransactions", lastFiveTransactions);
        model.addAttribute("singleTransactionDto", new SingleTransactionDto());
    }

    private void initBasicAddConstantTransactionModelAttributes(Model model) {
        List<ConstantTransaction> constantTransactions = constantTransactionService.findAllActiveConstantTransactions();
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
