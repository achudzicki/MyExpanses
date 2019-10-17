package com.chudzick.expanses.controllers.analysis;

import com.chudzick.expanses.domain.ApplicationActions;
import com.chudzick.expanses.domain.expanses.ConstantTransaction;
import com.chudzick.expanses.domain.expanses.Cycle;
import com.chudzick.expanses.domain.expanses.SingleTransaction;
import com.chudzick.expanses.domain.expanses.UserTransactions;
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
import com.chudzick.expanses.util.ListsUnion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(value = "analysis")
public class AnalysisController {

    @Autowired
    private ChartService chartService;

    @Autowired
    private CycleService cycleService;

    @Autowired
    private SingleTransactionService<SingleTransaction, SingleTransactionDto> singleTransactionService;

    @Autowired
    private ConstantTransactionService<ConstantTransaction, ConstantTransactionDto> constantTransactionDto;


    @GetMapping(value = "actual")
    public String actualCycleAnalysis(Model model) throws NoActiveCycleException {
        Cycle cycle = cycleService.findActiveCycle().orElseThrow(() -> new NoActiveCycleException(ApplicationActions.SHOW_ANALYSIS));
        List<SingleTransaction> singleTransactions = singleTransactionService.findAll();
        List<ConstantTransaction> constantTransactions = constantTransactionDto.findAll();
        List<UserTransactions> allTransactions = ListsUnion.union(singleTransactions,constantTransactions);
        ActualTransactionStats stats = new ActualTransactionStatsFactory().fromTransactionList(allTransactions, Optional.of(cycle));

        model.addAttribute("cycleDisplayInfo", CycleInformation.fromCycle(cycle));
        model.addAttribute("actualTransactionStats", stats);
        model.addAttribute("expansePerDayChartData", chartService.prepareTransactionPerDayChart(cycle, singleTransactions, constantTransactions));
        model.addAttribute("expansePerGroupChartData", chartService.prepareExpansePerGroupChart(allTransactions));
        model.addAttribute("saveGoal", cycle.getSaveGoal());
        return "analysis/actualCycleAnalysis";
    }
}
