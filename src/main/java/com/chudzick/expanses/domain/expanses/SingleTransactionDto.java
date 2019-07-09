package com.chudzick.expanses.domain.expanses;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class SingleTransactionDto {

    @NotNull(message = "{form.validation.not.empty}")
    @DecimalMin( value = "0.01", message = "Kwota nie może być mniejsza od zera")
    private BigDecimal amound;

    private String transactionDate;

    private TransactionType transactionType;

    private TransactionGroup transactionGroup;

    public BigDecimal getAmound() {
        return amound;
    }

    public void setAmound(BigDecimal amound) {
        this.amound = amound;
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
