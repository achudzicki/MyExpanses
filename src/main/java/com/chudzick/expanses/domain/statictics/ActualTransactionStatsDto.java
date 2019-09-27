package com.chudzick.expanses.domain.statictics;

import java.math.BigDecimal;

public class ActualTransactionStatsDto {
    private int expensesCnt;
    private int incomeCnt;
    private BigDecimal expensesSum;
    private BigDecimal incomeSum;
    private BigDecimal balance;

    public ActualTransactionStatsDto() {
        expensesSum = BigDecimal.ZERO;
        incomeSum = BigDecimal.ZERO;
        balance = BigDecimal.ZERO;
    }

    public void incrementExpensesCnt() {
        this.expensesCnt++;
    }

    public void incrementIncomeCnt() {
        this.incomeCnt++;
    }

    public void updateExpensesSum(BigDecimal expensesSum) {
        this.expensesSum = this.incomeSum.subtract(expensesSum);
    }

    public void updateIncomeSum(BigDecimal incomeSum) {
        this.incomeSum = this.incomeSum.subtract(incomeSum);
    }

    public void updateBalance(BigDecimal balance) {
        this.balance = this.balance.subtract(balance);
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
