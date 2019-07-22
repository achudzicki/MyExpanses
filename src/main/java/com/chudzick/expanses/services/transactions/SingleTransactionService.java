package com.chudzick.expanses.services.transactions;

import com.chudzick.expanses.domain.expanses.SingleTransaction;
import com.chudzick.expanses.domain.expanses.SingleTransactionDto;

import java.util.List;

public interface SingleTransactionService {

    SingleTransaction addNewTransaction(SingleTransactionDto transactionDto);

    List<SingleTransaction> findLastSingleTransactionsLimitBy(int limit);

    int countTransactionsByGroup(long groupId);

    List<SingleTransaction> findAllByGroupId(long groupId);

    List<SingleTransaction> findAll();

    void deleteTransactionById(long transactionId);
}
