package com.chudzick.expanses.factories;

import com.chudzick.expanses.domain.expanses.SingleTransaction;
import com.chudzick.expanses.domain.expanses.TransactionType;
import com.chudzick.expanses.domain.statictics.ActualTransactionStats;

import java.math.BigDecimal;
import java.util.List;

public class ActualTransactionStatsFactory {
    private int expensesCnt;
    private int incomeCnt;
    private BigDecimal expensesSum;
    private BigDecimal incomeSum;
    private BigDecimal balance;

    public ActualTransactionStatsFactory() {
        expensesSum = BigDecimal.ZERO;
        incomeSum = BigDecimal.ZERO;
        balance = BigDecimal.ZERO;
    }

    public ActualTransactionStats fromTransactionList(List<SingleTransaction> allTransactions) {


        allTransactions.stream().forEach(singleTransaction -> {

            if (singleTransaction.getTransactionType().equals(TransactionType.INCOME)) {
                incomeCnt++;
                incomeSum = incomeSum.add(singleTransaction.getAmound());
                balance = balance.add(singleTransaction.getAmound());
            } else {
                expensesCnt++;
                expensesSum = expensesSum.add(singleTransaction.getAmound());
                balance = balance.subtract(singleTransaction.getAmound());
            }
        });
        return new ActualTransactionStats(expensesCnt, incomeCnt, expensesSum, incomeSum, balance);
    }
}
