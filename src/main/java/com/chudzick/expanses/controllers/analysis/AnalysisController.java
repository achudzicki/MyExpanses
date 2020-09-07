package com.chudzick.expanses.controllers.analysis;

import com.chudzick.expanses.domain.ApplicationActions;
import com.chudzick.expanses.domain.analysis.AverageExpanse;
import com.chudzick.expanses.domain.analysis.GroupExcludeDto;
import com.chudzick.expanses.domain.expanses.*;
import com.chudzick.expanses.domain.expanses.dto.ConstantTransactionDto;
import com.chudzick.expanses.domain.expanses.dto.SingleTransactionDto;
import com.chudzick.expanses.domain.informations.CycleInformation;
import com.chudzick.expanses.domain.statictics.ActualTransactionStats;
import com.chudzick.expanses.exceptions.NoActiveCycleException;
import com.chudzick.expanses.factories.ActualTransactionStatsFactory;
import com.chudzick.expanses.services.analysis.AnalysisService;
import com.chudzick.expanses.services.charts.ChartService;
import com.chudzick.expanses.services.transactions.ConstantTransactionService;
import com.chudzick.expanses.services.transactions.CycleService;
import com.chudzick.expanses.services.transactions.SingleTransactionService;
import com.chudzick.expanses.services.transactions.TransactionGroupService;
import com.chudzick.expanses.util.ListsUnion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
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

    @Autowired
    private AnalysisService analysisService;


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
        List<AverageExpanse> averageExpanseList = analysisService.getAverageExpansesList();

        model.addAttribute("averageExpanses", averageExpanseList);
        model.addAttribute("userGroups", userGroups);
        model.addAttribute("chartData", chartService.prepareAverageTransactionPerGroupChart(averageExpanseList));
        return "analysis/averageExpanses";
    }

    @PostMapping(value = "average/groups")
    public String averageGroupsExpansesPost(@ModelAttribute("groupExcludeDto") GroupExcludeDto groupExcludeDto, Model model) {
        List<TransactionGroup> userGroups = transactionGroupService.getAllGroups();
        Set<Long> excludeId = groupExcludeDto.getExcludedIds();
        List<AverageExpanse> averageExpanseList = analysisService.getAverageExpansesList();

        model.addAttribute("averageExpanses", averageExpanseList);
        model.addAttribute("userGroups", userGroups);
        model.addAttribute("chartData", chartService.prepareAverageTransactionPerGroupChart(averageExpanseList
                .stream()
                .filter(avg -> !excludeId.contains(avg.getTransactionGroup().getId()))
                .collect(Collectors.toList())));
        model.addAttribute("excludedGroups",excludeId);
        return "analysis/averageExpanses";
    }

}
