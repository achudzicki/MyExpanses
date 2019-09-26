package com.chudzick.expanses.beans.transactions;

import com.chudzick.expanses.domain.expanses.SingleTransactionDto;

import java.util.List;

public class ImportOperationFormBean {
    List<SingleTransactionDto> transactionsDto;

    public List<SingleTransactionDto> getTransactionsDto() {
        return transactionsDto;
    }

    public void setTransactionsDto(List<SingleTransactionDto> transactionsDto) {
        this.transactionsDto = transactionsDto;
    }
}
