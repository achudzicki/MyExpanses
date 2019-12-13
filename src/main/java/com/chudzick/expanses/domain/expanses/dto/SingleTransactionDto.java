package com.chudzick.expanses.domain.expanses.dto;

import com.chudzick.expanses.domain.expanses.Cycle;
import com.chudzick.expanses.domain.expanses.TransactionGroup;
import com.chudzick.expanses.domain.expanses.TransactionType;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class SingleTransactionDto {

    @NotNull(message = "{form.validation.not.empty}")
    @DecimalMin( value = "0.01", message = "Kwota nie może być mniejsza od zera")
    private BigDecimal amount;

    private String transactionDate;

    private TransactionType transactionType;

    private TransactionGroup transactionGroup;

    private Cycle cycle;

    private String importedTransactionTitle;

    public SingleTransactionDto() {
    }

    public SingleTransactionDto(Cycle cycle) {
        this.cycle = cycle;
    }

    public String getImportedTransactionTitle() {
        return importedTransactionTitle;
    }

    public void setImportedTransactionTitle(String importedTransactionTitle) {
        this.importedTransactionTitle = importedTransactionTitle;
    }

    public void setCycle(Cycle cycle) {
        this.cycle = cycle;
    }

    public Cycle getCycle() {
        return cycle;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public TransactionGroup getTransactionGroup() {
        return transactionGroup;
    }

    public void setTransactionGroup(TransactionGroup transactionGroup) {
        this.transactionGroup = transactionGroup;
    }
}
