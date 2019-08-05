package com.chudzick.expanses.services.transactions;

import com.chudzick.expanses.domain.expanses.*;
import com.chudzick.expanses.exceptions.NoActiveCycleException;
import com.chudzick.expanses.exceptions.UserNotPermittedToActionException;

import java.util.List;

public interface UserTransactionService {

    SingleTransaction addNewSingleTransaction(SingleTransactionDto transactionDto) throws NoActiveCycleException;

    List<SingleTransaction> findLastSingleTransactionsLimitBy(int limit);

    int countTransactionsByGroup(long groupId);

    List<SingleTransaction> findAllSingleTransactionsByGroupId(long groupId);

    List<UserTransactions> findAll();

    boolean deleteSingleTransactionById(long transactionId) throws UserNotPermittedToActionException;

    List<ConstantTransaction> findAllActiveConstantTransactions();

    ConstantTransaction addNewConstantTransaction(ConstantTransactionDto constantTransactionDto) throws NoActiveCycleException;
}
