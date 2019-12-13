package com.chudzick.expanses.domain.filter.transactions;

import com.chudzick.expanses.domain.expanses.TransactionType;

public class TransactionFilterRequest {
    private String dateFrom;
    private String dateTo;
    private TransactionType transactionType;
    private Long transactionGroup;

    public String getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(String dateFrom) {
        this.dateFrom = dateFrom;
    }

    public String getDateTo() {
        return dateTo;
    }

    public void setDateTo(String dateTo) {
        this.dateTo = dateTo;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public Long getTransactionGroup() {
        return transactionGroup;
    }

    public void setTransactionGroup(Long transactionGroup) {
        this.transactionGroup = transactionGroup;
    }
}
