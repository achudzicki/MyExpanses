package com.chudzick.expanses.services.charts;

import com.chudzick.expanses.beans.chart.TransactionPerDayChartBean;
import com.chudzick.expanses.domain.expanses.ConstantTransaction;
import com.chudzick.expanses.domain.expanses.Cycle;
import com.chudzick.expanses.domain.expanses.SingleTransaction;
import com.chudzick.expanses.domain.expanses.TransactionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class ChartServiceImpl implements ChartService {

    @Autowired
    private TransactionPerDayChartBean transactionPerDayChartBean;

    @Override
    public TransactionPerDayChartBean prepareTransactionPerDayChart(Cycle cycle, Collection<SingleTransaction> singleTransactions, Collection<ConstantTransaction> constantTransactions) {
        Map<String, BigDecimal> incomesMap = new LinkedHashMap<>();
        Map<String, BigDecimal> expansesMap = new LinkedHashMap<>();
        String firstKey = null;
        for (LocalDate i = cycle.getDateFrom(); i.isBefore(cycle.getDateTo()) || i.isEqual(cycle.getDateTo()); i = i.plusDays(1)) {
            if (firstKey == null) {
                firstKey = i.toString();
            }
            incomesMap.put(i.toString(), BigDecimal.ZERO);
            expansesMap.put(i.toString(), BigDecimal.ZERO);
        }

        for (SingleTransaction transaction : singleTransactions) {
            if (transaction.getTransactionType().equals(TransactionType.INCOME)) {
                BigDecimal temp = incomesMap.get(transaction.getTransactionDate().toString()).add(transaction.getAmount());
                incomesMap.put(transaction.getTransactionDate().toString(), temp);
            } else {
                BigDecimal temp = expansesMap.get(transaction.getTransactionDate().toString()).add(transaction.getAmount());
                expansesMap.put(transaction.getTransactionDate().toString(), temp);
            }
        }

        for (ConstantTransaction transaction : constantTransactions) {
            if (transaction.getTransactionType().equals(TransactionType.INCOME)) {
                BigDecimal temp = incomesMap.get(firstKey).add(transaction.getAmount());
                incomesMap.put(firstKey, temp);
            } else {
                BigDecimal temp = expansesMap.get(firstKey).add(transaction.getAmount());
                expansesMap.put(firstKey, temp);
            }
        }


        transactionPerDayChartBean.setIncomes(incomesMap.values().toArray(new BigDecimal[]{}));
        transactionPerDayChartBean.setExpanses(expansesMap.values().toArray(new BigDecimal[]{}));
        transactionPerDayChartBean.setDates(incomesMap.keySet().toArray(new String[]{}));
        return transactionPerDayChartBean;
    }
}
