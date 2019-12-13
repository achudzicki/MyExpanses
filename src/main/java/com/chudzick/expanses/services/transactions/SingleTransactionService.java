package com.chudzick.expanses.services.transactions;

import com.chudzick.expanses.domain.expanses.UserTransactions;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface SingleTransactionService<E extends UserTransactions, T> extends UserTransactionService<E, T> {

    List<E> findLastSingleTransactionsLimitBy(int limit);

    List<E> findFilteredTransactions(Specification<E> build);
}
