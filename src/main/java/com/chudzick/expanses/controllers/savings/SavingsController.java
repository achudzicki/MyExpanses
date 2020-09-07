package com.chudzick.expanses.controllers.savings;

import com.chudzick.expanses.beans.chart.SavingsPerCycleChartBean;
import com.chudzick.expanses.domain.expanses.Cycle;
import com.chudzick.expanses.domain.expanses.UserTransactions;
import com.chudzick.expanses.domain.informations.CycleInformation;
import com.chudzick.expanses.domain.statictics.ActualTransactionStats;
import com.chudzick.expanses.factories.ActualTransactionStatsFactory;
import com.chudzick.expanses.services.transactions.CycleService;
import com.chudzick.expanses.util.ListsUnion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping(value = "savings")
public class SavingsController {

    @Autowired
    private CycleService cycleService;

    @Autowired
    private SavingsPerCycleChartBean savingsPerCycleChartBean;

    @GetMapping(value = "all")
    public String getAllUserSavings(Model model) {
        List<Cycle> allUserCycles = cycleService.findAllUserCycles();
        final ActualTransactionStatsFactory statsFactory = new ActualTransactionStatsFactory();
        BigDecimal planningSavings = BigDecimal.ZERO;
        BigDecimal actualSavings = BigDecimal.ZERO;
        Map<String, BigDecimal> planningSavingsChartData = new LinkedHashMap<>();
        Map<String, BigDecimal> actualSavingsChartData = new LinkedHashMap<>();
        Map<String, ActualTransactionStats> statisticsPerCycle = new LinkedHashMap<>();

        for (Cycle cycle : allUserCycles) {
            String cycleDisplayName = CycleInformation.fromCycle(cycle).getCycleDisplayName();
            planningSavings = planningSavings.add(cycle.getSaveGoal());
            planningSavingsChartData.put(cycleDisplayName, planningSavings);
            List<UserTransactions> allTransactions = ListsUnion.union(cycle.getConstantTransactions(), cycle.getCycleTransactions());
            ActualTransactionStats stats = statsFactory.fromTransactionList(allTransactions, Optional.of(cycle));
            actualSavings = actualSavings.add(stats.getBalance().add(cycle.getSaveGoal()));
            actualSavingsChartData.put(cycleDisplayName, actualSavings);
            statisticsPerCycle.put(cycleDisplayName, stats);
        }

        savingsPerCycleChartBean.setActualSavings(actualSavingsChartData.values().toArray(new BigDecimal[]{}));
        savingsPerCycleChartBean.setPlanningSavings(planningSavingsChartData.values().toArray(new BigDecimal[]{}));
        savingsPerCycleChartBean.setCycleName(actualSavingsChartData.keySet().toArray(new String[]{}));

        model.addAttribute("chartData",savingsPerCycleChartBean);
        model.addAttribute("planningSavings", planningSavings);
        model.addAttribute("actualSavings", actualSavings);
        model.addAttribute("statisticsPerCycle", statisticsPerCycle);
        return "savings/allTimeSavings";
    }
}
