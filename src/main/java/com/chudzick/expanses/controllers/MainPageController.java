package com.chudzick.expanses.controllers;

import com.chudzick.expanses.domain.expanses.Cycle;
import com.chudzick.expanses.domain.expanses.SingleTransaction;
import com.chudzick.expanses.domain.informations.CycleInformation;
import com.chudzick.expanses.domain.statictics.ActualTransactionStats;
import com.chudzick.expanses.factories.ActualTransactionStatsFactory;
import com.chudzick.expanses.services.transactions.CycleService;
import com.chudzick.expanses.services.transactions.SingleTransactionService;
import com.chudzick.expanses.services.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class MainPageController {

    private static final int MAIN_PAGE_TRANSACTIONS = 5;
    @Autowired
    private UserService userService;

    @Autowired
    private SingleTransactionService singleTransactionService;

    @Autowired
    private CycleService cycleService;

    @GetMapping(value = "/")
    public String initMainPage(Model model) {
        Optional<Cycle> activeCycle = cycleService.findActiveCycle();

        if (!activeCycle.isPresent()) {
            return "redirect:/settings";
        }

        List<SingleTransaction> allTransactions = singleTransactionService.findAll();
        List<SingleTransaction> lastFiveTransactions = allTransactions.
                stream()
                .sorted((id1, id2) -> Long.compare(id2.getId(), id1.getId()))
                .collect(Collectors.toList());

        ActualTransactionStats actualTransactionStats = new ActualTransactionStatsFactory().fromTransactionList(allTransactions);
        CycleInformation cycleInformation = CycleInformation.fromCycle(activeCycle.get());


        model.addAttribute("lastTransactions", lastFiveTransactions);
        model.addAttribute("actualTransactionStats", actualTransactionStats);
        model.addAttribute("cycleInformation", cycleInformation);
        return "mainPage";
    }
}
