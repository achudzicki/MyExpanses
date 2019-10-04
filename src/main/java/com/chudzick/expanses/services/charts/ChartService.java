package com.chudzick.expanses.services.charts;

import com.chudzick.expanses.beans.chart.TransactionPerDayChartBean;
import com.chudzick.expanses.domain.expanses.ConstantTransaction;
import com.chudzick.expanses.domain.expanses.Cycle;
import com.chudzick.expanses.domain.expanses.SingleTransaction;

import java.util.Collection;

public interface ChartService {

    TransactionPerDayChartBean prepareTransactionPerDayChart(Cycle cycle, Collection<SingleTransaction> singleTransactions, Collection<ConstantTransaction> constantTransactions);
}
