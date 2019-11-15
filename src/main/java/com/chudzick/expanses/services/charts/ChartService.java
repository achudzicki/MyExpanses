package com.chudzick.expanses.services.charts;

import com.chudzick.expanses.beans.chart.AverageTransactionsPerGroupRadarChart;
import com.chudzick.expanses.beans.chart.ExpansePerTransactionGroupBean;
import com.chudzick.expanses.beans.chart.TransactionPerDayChartBean;
import com.chudzick.expanses.domain.analysis.AverageExpanse;
import com.chudzick.expanses.domain.expanses.ConstantTransaction;
import com.chudzick.expanses.domain.expanses.Cycle;
import com.chudzick.expanses.domain.expanses.SingleTransaction;
import com.chudzick.expanses.domain.expanses.UserTransactions;

import java.util.Collection;
import java.util.List;

public interface ChartService {

    TransactionPerDayChartBean prepareTransactionPerDayChart(Cycle cycle, Collection<SingleTransaction> singleTransactions, Collection<ConstantTransaction> constantTransactions);

    ExpansePerTransactionGroupBean prepareExpansePerGroupChart(List<UserTransactions> userTransactions);

    AverageTransactionsPerGroupRadarChart prepareAverageTransactionPerGroupChart(List<AverageExpanse> averageExpanseList);
}
