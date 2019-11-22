package com.chudzick.expanses.services.transactions;

import com.chudzick.expanses.domain.expanses.UserTransactions;
import com.chudzick.expanses.exceptions.AppObjectNotFoundException;
import com.chudzick.expanses.exceptions.NoActiveCycleException;
import com.chudzick.expanses.exceptions.UserNotPermittedToActionException;

import java.util.List;
import java.util.Optional;

public interface UserTransactionService<E extends UserTransactions, T> {

    E addNewTransaction(T dto) throws NoActiveCycleException;

    int countTransactionsByGroup(long groupId);

    List<E> findAllTransactionsByGroupId(long groupId);

    boolean deleteTransactionById(long transactionId) throws UserNotPermittedToActionException, AppObjectNotFoundException;

    Optional<E> findTransactionById(long transactionId);

    List<E> findAll();

    boolean addAll(List<T> list) throws NoActiveCycleException;

    List<E> findAllUserTransactions();
}
