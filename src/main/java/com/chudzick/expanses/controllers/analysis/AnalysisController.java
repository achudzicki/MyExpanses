package com.chudzick.expanses.controllers.analysis;

import com.chudzick.expanses.domain.ApplicationActions;
import com.chudzick.expanses.domain.analysis.AverageExpanse;
import com.chudzick.expanses.domain.expanses.*;
import com.chudzick.expanses.domain.expanses.dto.ConstantTransactionDto;
import com.chudzick.expanses.domain.expanses.dto.SingleTransactionDto;
import com.chudzick.expanses.domain.informations.CycleInformation;
import com.chudzick.expanses.domain.statictics.ActualTransactionStats;
import com.chudzick.expanses.exceptions.NoActiveCycleException;
import com.chudzick.expanses.factories.ActualTransactionStatsFactory;
import com.chudzick.expanses.services.charts.ChartService;
import com.chudzick.expanses.services.transactions.ConstantTransactionService;
import com.chudzick.expanses.services.transactions.CycleService;
import com.chudzick.expanses.services.transactions.SingleTransactionService;
import com.chudzick.expanses.services.transactions.TransactionGroupService;
import com.chudzick.expanses.util.ListsUnion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping(value = "analysis")
public class AnalysisController {

    @Autowired
    private ChartService chartService;

    @Autowired
    private CycleService cycleService;

    @Autowired
    private TransactionGroupService transactionGroupService;

    @Autowired
    private SingleTransactionService<SingleTransaction, SingleTransactionDto> singleTransactionService;

    @Autowired
    private ConstantTransactionService<ConstantTransaction, ConstantTransactionDto> constantTransactionService;


    @GetMapping(value = "actual")
    public String actualCycleAnalysis(Model model) throws NoActiveCycleException {
        Cycle cycle = cycleService.findActiveCycle().orElseThrow(() -> new NoActiveCycleException(ApplicationActions.SHOW_ANALYSIS));
        List<SingleTransaction> singleTransactions = singleTransactionService.findAll();
        List<ConstantTransaction> constantTransactions = constantTransactionService.findAll();
        List<UserTransactions> allTransactions = ListsUnion.union(singleTransactions, constantTransactions);
        ActualTransactionStats stats = new ActualTransactionStatsFactory().fromTransactionList(allTransactions, Optional.of(cycle));

        model.addAttribute("cycleDisplayInfo", CycleInformation.fromCycle(cycle));
        model.addAttribute("actualTransactionStats", stats);
        model.addAttribute("expansePerDayChartData", chartService.prepareTransactionPerDayChart(cycle, singleTransactions, constantTransactions));
        model.addAttribute("expansePerGroupChartData", chartService.prepareExpansePerGroupChart(allTransactions));
        model.addAttribute("saveGoal", cycle.getSaveGoal());
        return "analysis/actualCycleAnalysis";
    }


    @GetMapping(value = "average/groups")
    public String averageGroupsExpanses(Model model) {
        List<TransactionGroup> userGroups = transactionGroupService.getAllGroups();
        List<SingleTransaction> allUserTransactions = singleTransactionService.findAllUserTransactions();
        List<ConstantTransaction> allUserConstantTransaction = constantTransactionService.findAllUserTransactions();
        List<UserTransactions> allTransactions = ListsUnion.union(allUserConstantTransaction, allUserTransactions);
        long userCyclesCnt = cycleService.countUserCycles();


        Map<Long, AverageExpanse> tempMap = new HashMap<>();
        for (UserTransactions transaction : allTransactions) {
            AverageExpanse temp = tempMap.get(transaction.getTransactionGroup().getId());
            if (temp == null) {
                tempMap.put(transaction.getTransactionGroup().getId(), new AverageExpanse(transaction, userCyclesCnt));
            } else {
                temp.update(transaction);
            }
        }

        model.addAttribute("averageExpanses", tempMap.values());
        model.addAttribute("userGroups", userGroups);
        return "analysis/averageExpanses";
    }
}
