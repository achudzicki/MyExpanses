package com.chudzick.expanses.services.transactions;

import com.chudzick.expanses.domain.expanses.ConstantTransaction;
import com.chudzick.expanses.domain.expanses.Cycle;
import com.chudzick.expanses.domain.expanses.UserTransactions;
import com.chudzick.expanses.domain.expanses.dto.ConstTransactionDeleteDto;
import com.chudzick.expanses.domain.users.AppUser;
import com.chudzick.expanses.exceptions.AppObjectNotFoundException;
import com.chudzick.expanses.exceptions.UserNotPermittedToActionException;

import java.util.List;

public interface ConstantTransactionService<E extends UserTransactions, T> extends UserTransactionService<E, T> {

    List<E> findAllActiveConstantTransactions();

    void deleteTransactionsFromCycles(long transactionId, List<ConstTransactionDeleteDto> constantTransDto) throws AppObjectNotFoundException, UserNotPermittedToActionException;

   List<ConstantTransaction> renewConstantTransactions(Cycle newCycle, AppUser appUser);
}
