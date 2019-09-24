package com.chudzick.expanses.services.transactions;

import com.chudzick.expanses.domain.expanses.SingleTransaction;
import com.chudzick.expanses.domain.expanses.UserTransactions;

import java.util.List;

public interface SingleTransactionService<E extends UserTransactions, T> extends UserTransactionService<E, T> {

    List<SingleTransaction> findLastSingleTransactionsLimitBy(int limit);
}
