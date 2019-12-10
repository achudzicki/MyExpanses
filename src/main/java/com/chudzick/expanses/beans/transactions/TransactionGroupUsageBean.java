package com.chudzick.expanses.beans.transactions;

import com.chudzick.expanses.domain.expanses.ConstantTransaction;
import com.chudzick.expanses.domain.expanses.SingleTransaction;
import com.chudzick.expanses.domain.expanses.TransactionGroup;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

@Component
@Scope(scopeName = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class TransactionGroupUsageBean {

    private TransactionGroup transactionGroup;
    private List<SingleTransaction> groupSingleTransactions;
    private List<ConstantTransaction> constantTransactions;

    public List<ConstantTransaction> getConstantTransactions() {
        return constantTransactions;
    }

    public void setConstantTransactions(List<ConstantTransaction> constantTransactions) {
        this.constantTransactions = constantTransactions;
    }

    public TransactionGroup getTransactionGroup() {
        return transactionGroup;
    }

    public void setTransactionGroup(TransactionGroup transactionGroup) {
        this.transactionGroup = transactionGroup;
    }

    public List<SingleTransaction> getGroupSingleTransactions() {
        return groupSingleTransactions;
    }

    public void setGroupSingleTransactions(List<SingleTransaction> groupSingleTransactions) {
        this.groupSingleTransactions = groupSingleTransactions;
    }
}
