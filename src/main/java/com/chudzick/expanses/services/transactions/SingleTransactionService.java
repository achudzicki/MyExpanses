package com.chudzick.expanses.services.transactions;

import com.chudzick.expanses.domain.expanses.SingleTransaction;
import com.chudzick.expanses.domain.expanses.SingleTransactionDto;

public interface SingleTransactionService {

    SingleTransaction addNewTransaction(SingleTransactionDto transactionDto);
}
