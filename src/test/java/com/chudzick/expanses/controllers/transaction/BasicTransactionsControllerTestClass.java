package com.chudzick.expanses.controllers.transaction;

import com.chudzick.expanses.TestSingleTransactionSupplier;
import com.chudzick.expanses.TestTransactionGroupsSupplier;
import com.chudzick.expanses.TestUserSupplier;
import com.chudzick.expanses.domain.expanses.Cycle;
import com.chudzick.expanses.domain.expanses.SingleTransaction;
import com.chudzick.expanses.domain.expanses.TransactionGroup;
import com.chudzick.expanses.domain.users.AppUser;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public abstract class BasicTransactionsControllerTestClass implements TestSingleTransactionSupplier, TestUserSupplier, TestTransactionGroupsSupplier {
    protected static final String NOTIFICATIONS_ATTR_NAME = "notifications";

    protected List<TransactionGroup> prepareTransactionGroupList(int listSize, AppUser appUser) {
        List<TransactionGroup> transactionGroups = new ArrayList<>();

        for (int i = 0; i < listSize; i++) {
            transactionGroups.add(prepareTransactionGroup(appUser));
        }

        return transactionGroups;
    }

    protected List<SingleTransaction> prepareListOfTransactions(int listSize, Cycle mockCycle, AppUser appUser) {
        List<SingleTransaction> transactions = new ArrayList<>();
        for (int i = 0; i < listSize; i++) {
            transactions.add(prepareSingleTransaction(mockCycle, appUser));
        }
        return transactions;
    }

    protected List<SingleTransaction> prepareListOfAllTransactions(int listSize, Cycle mockCycle, AppUser appUser) {
        List<SingleTransaction> transactions = new ArrayList<>();
        for (int i = 0; i < listSize; i++) {
            transactions.add(prepareSingleTransaction(mockCycle, appUser));
        }
        return transactions;
    }

    protected Cycle prepareMockCycle(AppUser appUser) {
        Cycle cycle = new Cycle();

        cycle.setActive(true);
        cycle.setAppUser(appUser);
        cycle.setSaveGoal(BigDecimal.ONE);
        cycle.setDateFrom(LocalDate.now().minusDays(5));
        cycle.setDateTo(LocalDate.now().plusDays(25));

        return cycle;
    }

}
