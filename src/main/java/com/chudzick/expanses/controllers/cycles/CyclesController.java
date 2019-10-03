package com.chudzick.expanses.controllers.cycles;

import com.chudzick.expanses.domain.expanses.ConstantTransaction;
import com.chudzick.expanses.domain.expanses.Cycle;
import com.chudzick.expanses.domain.expanses.SingleTransaction;
import com.chudzick.expanses.domain.expanses.UserTransactions;
import com.chudzick.expanses.domain.expanses.dto.ConstantTransactionDto;
import com.chudzick.expanses.domain.expanses.dto.SingleTransactionDto;
import com.chudzick.expanses.domain.informations.CycleInformation;
import com.chudzick.expanses.domain.paging.RequestPage;
import com.chudzick.expanses.domain.statictics.ActualTransactionStats;
import com.chudzick.expanses.exceptions.NoActiveCycleException;
import com.chudzick.expanses.factories.ActualTransactionStatsFactory;
import com.chudzick.expanses.factories.paging.PageFactory;
import com.chudzick.expanses.services.transactions.ConstantTransactionService;
import com.chudzick.expanses.services.transactions.CycleService;
import com.chudzick.expanses.services.transactions.SingleTransactionService;
import com.chudzick.expanses.util.ListsUnion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Controller
@RequestMapping(value = "cycle")
public class CyclesController {

    @Autowired
    private CycleService cycleService;

    @Autowired
    private SingleTransactionService<SingleTransaction, SingleTransactionDto> singleTransactionsService;

    @Autowired
    private ConstantTransactionService<ConstantTransaction, ConstantTransactionDto> constantTransactionService;

    @GetMapping(value = "/archive")
    public String getArchiveCycles(Model model) {
        List<Cycle> archiveCycles = cycleService.findAllUserCycles();

        model.addAttribute("cyclesList", archiveCycles);
        return "cycles/archiveCycles";
    }

    @GetMapping(value = "/manage/{cycleId}")
    public String manageArchiveCycle(@PathVariable int cycleId, Model model) throws NoActiveCycleException {
        Cycle cycle = cycleService.findById(cycleId);
        List<SingleTransaction> singleTransactions = cycle.getCycleTransactions();
        Set<ConstantTransaction> constantTransactions = cycle.getConstantTransactions();
        List<UserTransactions> allTransactions = ListsUnion.union(singleTransactions, new ArrayList<>(constantTransactions));
        ActualTransactionStats stats = new ActualTransactionStatsFactory().fromTransactionList(allTransactions, Optional.of(cycle));
        RequestPage<SingleTransaction> page = new PageFactory<SingleTransaction>().getRequestPage(singleTransactions, 0, 100);


        model.addAttribute("cycleDisplayInfo", CycleInformation.fromCycle(cycle));
        model.addAttribute("saveGoal", cycle.getSaveGoal());
        model.addAttribute("transactionPage", page);
        model.addAttribute("constantTransactions", constantTransactions);
        model.addAttribute("actualTransactionStats", stats);
        return "cycles/manageArchiveCycle";
    }
}
