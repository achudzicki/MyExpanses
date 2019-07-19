package com.chudzick.expanses.domain.statictics;

import java.math.BigDecimal;

public class ActualTransactionStats {
    private int expensesCnt;
    private int incomeCnt;
    private BigDecimal expensesSum;
    private BigDecimal incomeSum;
    private BigDecimal balance;

    public ActualTransactionStats(int expensesCnt, int incomeCnt, BigDecimal expensesSum, BigDecimal incomeSum, BigDecimal balance) {
        this.expensesCnt = expensesCnt;
        this.incomeCnt = incomeCnt;
        this.expensesSum = expensesSum;
        this.incomeSum = incomeSum;
        this.balance = balance;
    }

    public int getExpensesCnt() {
        return expensesCnt;
    }

    public int getIncomeCnt() {
        return incomeCnt;
    }

    public BigDecimal getExpensesSum() {
        return expensesSum;
    }

    public BigDecimal getIncomeSum() {
        return incomeSum;
    }

    public BigDecimal getBalance() {
        return balance;
    }
}
