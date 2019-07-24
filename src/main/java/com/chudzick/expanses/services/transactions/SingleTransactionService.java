package com.chudzick.expanses.services.transactions;

import com.chudzick.expanses.domain.expanses.SingleTransaction;
import com.chudzick.expanses.domain.expanses.SingleTransactionDto;
import com.chudzick.expanses.exceptions.NoActiveCycleException;
import com.chudzick.expanses.exceptions.UserNotPermittedToActionException;

import java.util.List;

public interface SingleTransactionService {

    SingleTransaction addNewTransaction(SingleTransactionDto transactionDto) throws NoActiveCycleException;

    List<SingleTransaction> findLastSingleTransactionsLimitBy(int limit);

    int countTransactionsByGroup(long groupId);

    List<SingleTransaction> findAllByGroupId(long groupId);

    List<SingleTransaction> findAll();

    boolean deleteTransactionById(long transactionId) throws UserNotPermittedToActionException;
}
