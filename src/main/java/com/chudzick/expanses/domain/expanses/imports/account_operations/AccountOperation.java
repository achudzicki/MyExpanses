package com.chudzick.expanses.domain.expanses.imports.account_operations;

public interface AccountOperation {
    public String getDate();

    public String getDescription();

    public String getOperationType();

    public double getAmount();
}
