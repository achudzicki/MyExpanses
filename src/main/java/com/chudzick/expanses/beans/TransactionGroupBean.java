package com.chudzick.expanses.beans;

import com.chudzick.expanses.domain.expanses.TransactionGroup;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Scope(scopeName = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class TransactionGroupBean {

    private List<TransactionGroup> usersTransactionsGroups;

    public List<TransactionGroup> getUsersTransactionsGroups() {
        return usersTransactionsGroups;
    }

    public void setUsersTransactionsGroups(List<TransactionGroup> usersTransactionsGroups) {
        this.usersTransactionsGroups = usersTransactionsGroups;
    }
}
