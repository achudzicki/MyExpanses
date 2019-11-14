package com.chudzick.expanses.domain.analysis;

import com.chudzick.expanses.domain.expanses.TransactionGroup;
import com.chudzick.expanses.domain.expanses.TransactionType;
import com.chudzick.expanses.domain.expanses.UserTransactions;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class AverageExpanse {
    private List<UserTransactions> incomeTransactions;
    private List<UserTransactions> expanseTransactions;
    private TransactionGroup transactionGroup;
    private BigDecimal totalBalance = BigDecimal.ZERO;
    private BigDecimal incomeBalance = BigDecimal.ZERO;
    private BigDecimal expanseBalance = BigDecimal.ZERO;
    private long cyclesCount;

    public AverageExpanse() {
    }

    public AverageExpanse(UserTransactions transaction, long userCyclesCnt) {
        incomeTransactions = new ArrayList<>();
        expanseTransactions = new ArrayList<>();

        if (transaction.getTransactionType().equals(TransactionType.INCOME)) {
            incomeTransactions.add(transaction);
            incomeBalance = transaction.getAmount();
        } else {
            expanseTransactions.add(transaction);
            expanseBalance = transaction.getAmount();
        }

        totalBalance = transaction.getAmount();
        transactionGroup = transaction.getTransactionGroup();
        cyclesCount = userCyclesCnt;
    }


    public List<UserTransactions> getIncomeTransactions() {
        return incomeTransactions;
    }

    public void setIncomeTransactions(List<UserTransactions> incomeTransactions) {
        this.incomeTransactions = incomeTransactions;
    }

    public List<UserTransactions> getExpanseTransactions() {
        return expanseTransactions;
    }

    public void setExpanseTransactions(List<UserTransactions> expanseTransactions) {
        this.expanseTransactions = expanseTransactions;
    }

    public TransactionGroup getTransactionGroup() {
        return transactionGroup;
    }

    public void setTransactionGroup(TransactionGroup transactionGroup) {
        this.transactionGroup = transactionGroup;
    }

    public BigDecimal getTotalBalance() {
        return totalBalance;
    }

    public void setTotalBalance(BigDecimal totalBalance) {
        this.totalBalance = totalBalance;
    }

    public BigDecimal getIncomeBalance() {
        return incomeBalance;
    }

    public void setIncomeBalance(BigDecimal incomeBalance) {
        this.incomeBalance = incomeBalance;
    }

    public BigDecimal getExpanseBalance() {
        return expanseBalance;
    }

    public void setExpanseBalance(BigDecimal expanseBalance) {
        this.expanseBalance = expanseBalance;
    }

    public long getCyclesCount() {
        return cyclesCount;
    }

    public void setCyclesCount(long cyclesCount) {
        this.cyclesCount = cyclesCount;
    }

    public void update(UserTransactions transaction) {
        if (transaction.getTransactionType().equals(TransactionType.INCOME)) {
            totalBalance = totalBalance.add(transaction.getAmount());
            incomeBalance = incomeBalance.add(transaction.getAmount());
            incomeTransactions.add(transaction);
        } else {
            totalBalance = totalBalance.subtract(transaction.getAmount());
            expanseBalance = expanseBalance.add(transaction.getAmount());
            expanseTransactions.add(transaction);
        }
    }

    public float getExpanseAverage() {
        return expanseBalance.floatValue() / expanseTransactions.size();
    }

    public float getIncomeAverage() {
        return incomeBalance.floatValue() / incomeTransactions.size();
    }

    public float getTotalAverageCycle() {
        return totalBalance.floatValue() / cyclesCount;
    }

    public float getTotalAverageTransaction() {
        return totalBalance.floatValue() / allTransactionCtn();
    }

    public int allTransactionCtn() {
        return incomeTransactions.size() + expanseTransactions.size();
    }
}
