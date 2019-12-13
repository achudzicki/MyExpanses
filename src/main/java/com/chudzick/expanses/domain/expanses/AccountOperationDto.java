package com.chudzick.expanses.domain.expanses;

import com.chudzick.expanses.domain.expanses.imports.account_operations.AccountOperation;

public final class AccountOperationDto {
    private String date;
    private String description;
    private String operationType;
    private double amount;
    private TransactionType transactionType;
    private TransactionGroup tipGroup;

    public TransactionGroup getTipGroup() {
        return tipGroup;
    }

    public void setTipGroup(TransactionGroup tipGroup) {
        this.tipGroup = tipGroup;
    }

    public long getTipGroupId() {
        if (tipGroup != null) {
            return tipGroup.getId();
        }
        return 0;
    }

    public String getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public String getOperationType() {
        return operationType;
    }

    public double getAmount() {
        return amount;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    private AccountOperationDto(String date, String description, String operationType, double amount, TransactionType transactionType) {
        this.date = date;
        this.description = description.replace("\n"," ");
        this.operationType = operationType;
        this.amount = amount;
        this.transactionType = transactionType;
    }

    public static AccountOperationDto from(AccountOperation operation) {
        return new AccountOperationDto(operation.getDate(), operation.getDescription(), operation.getOperationType(), Math.abs(operation.getAmount()),
                operation.getAmount() > 0 ? TransactionType.INCOME : TransactionType.EXPANSE);
    }
}
