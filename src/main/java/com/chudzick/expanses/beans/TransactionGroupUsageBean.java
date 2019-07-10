package com.chudzick.expanses.beans;

import com.chudzick.expanses.domain.expanses.SingleTransaction;
import com.chudzick.expanses.domain.expanses.TransactionGroup;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Scope(scopeName = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class TransactionGroupUsageBean {

    private TransactionGroup transactionGroup;
    private List<SingleTransaction> groupTransactions;

    public TransactionGroup getTransactionGroup() {
        return transactionGroup;
    }

    public void setTransactionGroup(TransactionGroup transactionGroup) {
        this.transactionGroup = transactionGroup;
    }

    public List<SingleTransaction> getGroupTransactions() {
        return groupTransactions;
    }

    public void setGroupTransactions(List<SingleTransaction> groupTransactions) {
        this.groupTransactions = groupTransactions;
    }
}
