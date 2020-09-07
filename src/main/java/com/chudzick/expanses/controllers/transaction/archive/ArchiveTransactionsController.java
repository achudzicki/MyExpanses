package com.chudzick.expanses.controllers.transaction.archive;

import com.chudzick.expanses.beans.responses.NotificationMessagesBean;
import com.chudzick.expanses.builders.NotificationMessageListBuilder;
import com.chudzick.expanses.domain.expanses.Cycle;
import com.chudzick.expanses.domain.expanses.SingleTransaction;
import com.chudzick.expanses.domain.expanses.TransactionGroup;
import com.chudzick.expanses.domain.expanses.TransactionType;
import com.chudzick.expanses.domain.expanses.dto.SingleTransactionDto;
import com.chudzick.expanses.domain.informations.CycleInformation;
import com.chudzick.expanses.domain.responses.SimpleNotificationMsg;
import com.chudzick.expanses.exceptions.NoActiveCycleException;
import com.chudzick.expanses.services.transactions.CycleService;
import com.chudzick.expanses.services.transactions.SingleTransactionService;
import com.chudzick.expanses.services.transactions.TransactionGroupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;


@RestController
@RequestMapping("archive")
public class ArchiveTransactionsController {
    private static final Logger LOG = LoggerFactory.getLogger(ArchiveTransactionsController.class);
    private static final String NOTIFICATIONS_ATTR_NAME = "notifications";
    private static final String NOTIFICATION_MESSAGES_BEAN_NAME = "notificationMessagesBean";
    private static final String TRANSACTION_ADD_ERROR_MSG = "Wystąpił problem podczas dodawania nowej transakcji";

    @Autowired
    private CycleService cycleService;

    @Autowired
    private TransactionGroupService transactionGroupService;

    @Autowired
    private NotificationMessagesBean notificationMessagesBean;

    @Autowired
    private SingleTransactionService<SingleTransaction, SingleTransactionDto> singleTransactionService;

    @GetMapping(value = "/transaction/{cycleId}")
    public String addArchiveTransactionGet(@PathVariable long cycleId, @ModelAttribute(NOTIFICATIONS_ATTR_NAME) List<SimpleNotificationMsg> notifications,
                                           Model model) throws NoActiveCycleException {
        initArchiveTransactionModel(cycleId, model);
        notificationMessagesBean.setNotificationsMessages(notifications);
        model.addAttribute(notificationMessagesBean);
        return "transaction/archive/addArchiveTransaction";
    }

    @PostMapping(value = "/transaction/add")
    public String addArchiveTransactionPost(@ModelAttribute @Valid SingleTransactionDto singleTransactionDto,
                                            BindingResult bindingResult, Model model, RedirectAttributes ra) throws NoActiveCycleException {
        if (bindingResult.hasErrors()) {
            notificationMessagesBean.setNotificationsMessages(new NotificationMessageListBuilder()
                    .withFailureNotificationMsg(TRANSACTION_ADD_ERROR_MSG)
                    .getNotificationList());
            model.addAttribute(NOTIFICATION_MESSAGES_BEAN_NAME, notificationMessagesBean);
            initArchiveTransactionModel(singleTransactionDto.getCycle().getId(), model);
            LOG.warn("Problem with adding new transaction, bindingResult Errors");
            return "transaction/archive/addArchiveTransaction";
        }

        singleTransactionService.addNewTransaction(singleTransactionDto);
        LOG.info(String.format("Successfully added transaction to cycle : %d", singleTransactionDto.getCycle().getId()));
        ra.addFlashAttribute(NOTIFICATIONS_ATTR_NAME, new NotificationMessageListBuilder()
                .withSuccessNotification("Poprawnie dodano transakcje do cyklu")
                .getNotificationList());

        return "redirect:/archive/transaction/" + singleTransactionDto.getCycle().getId();
    }

    @ModelAttribute(NOTIFICATIONS_ATTR_NAME)
    private List<SimpleNotificationMsg> defaultList() {
        return Collections.emptyList();
    }

    private void initArchiveTransactionModel(@PathVariable long cycleId, Model model) throws NoActiveCycleException {
        Cycle selectedCycle = cycleService.findById(cycleId);
        SingleTransactionDto dto = new SingleTransactionDto(selectedCycle);
        List<TransactionGroup> transactionGroups = transactionGroupService.getAllGroups();

        model.addAttribute("singleTransactionDto", dto);
        model.addAttribute("activeCycle", selectedCycle);
        model.addAttribute("cycleDisplayInfo", CycleInformation.fromCycle(selectedCycle));
        model.addAttribute("transactionGroups", transactionGroups);
        model.addAttribute("transactionTypes", TransactionType.values());
    }
}
