package com.chudzick.expanses.controllers;

import com.chudzick.expanses.beans.users.UserBean;
import com.chudzick.expanses.domain.expanses.ConstantTransaction;
import com.chudzick.expanses.domain.expanses.Cycle;
import com.chudzick.expanses.domain.expanses.SingleTransaction;
import com.chudzick.expanses.domain.expanses.UserTransactions;
import com.chudzick.expanses.domain.expanses.dto.ConstantTransactionDto;
import com.chudzick.expanses.domain.expanses.dto.SingleTransactionDto;
import com.chudzick.expanses.domain.informations.CycleInformation;
import com.chudzick.expanses.domain.statictics.ActualTransactionStats;
import com.chudzick.expanses.factories.ActualTransactionStatsFactory;
import com.chudzick.expanses.services.transactions.ConstantTransactionService;
import com.chudzick.expanses.services.transactions.CycleService;
import com.chudzick.expanses.services.transactions.SingleTransactionService;
import com.chudzick.expanses.services.users.UserService;
import com.chudzick.expanses.util.ListsUnion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class MainPageController {

    private static final int MAIN_PAGE_TRANSACTIONS = 5;

    @Autowired
    private SingleTransactionService<SingleTransaction, SingleTransactionDto> singleTransactionService;

    @Autowired
    private ConstantTransactionService<ConstantTransaction, ConstantTransactionDto> constantTransactionService;

    @Autowired
    private CycleService cycleService;

    @Autowired
    private UserBean userBean;

    @Autowired
    private UserService userService;

    @GetMapping(value = "/")
    public String initMainPage(Model model) {
        userBean.setAppUser(userService.getCurrentLogInUser());
        Optional<Cycle> activeCycle = cycleService.findActiveCycle();

        if (!activeCycle.isPresent()) {
            return "redirect:/settings";
        }

        List<SingleTransaction> allSingle = singleTransactionService.findAll();
        List<ConstantTransaction> allConstant = constantTransactionService.findAllActiveConstantTransactions();
        List<UserTransactions> allTransactions = ListsUnion.union(allConstant, allSingle);
        List<SingleTransaction> lastFiveTransactions = singleTransactionService.findLastSingleTransactionsLimitBy(MAIN_PAGE_TRANSACTIONS);

        Map<String, ActualTransactionStats> actualTransactionStats = new ActualTransactionStatsFactory().combineTransactionsFromList(allTransactions);
        CycleInformation cycleInformation = CycleInformation.fromCycle(activeCycle.get());


        model.addAttribute("saveGoal",activeCycle.get().getSaveGoal());
        model.addAttribute("lastTransactions", lastFiveTransactions);
        model.addAttribute("actualTransactionStats", actualTransactionStats);
        model.addAttribute("cycleInformation", cycleInformation);
        return "mainPage";
    }
}
