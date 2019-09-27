package com.chudzick.expanses.domain.expanses.dto;

import com.chudzick.expanses.domain.expanses.TransactionGroup;
import com.chudzick.expanses.domain.expanses.TransactionType;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class ConstantTransactionDto {

    @NotNull(message = "{form.validation.not.empty}")
    @DecimalMin(value = "0.01", message = "Kwota nie może być mniejsza od zera")
    private BigDecimal amount;
    private TransactionType transactionType;
    private TransactionGroup transactionGroup;
    private boolean permanentDuration;
    private Integer cyclesCount;
    private static final Integer CYCLES_APPEARS = 1;
    private boolean addToActiveCycle;

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
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

    public boolean isPermanentDuration() {
        return permanentDuration;
    }

    public void setPermanentDuration(boolean permanentDuration) {
        this.permanentDuration = permanentDuration;
    }

    public Integer getCyclesCount() {
        return cyclesCount;
    }

    public void setCyclesCount(Integer cyclesCount) {
        this.cyclesCount = cyclesCount;
    }

    public Integer getCyclesAppears() {
        return CYCLES_APPEARS;
    }

    public boolean isAddToActiveCycle() {
        return addToActiveCycle;
    }

    public void setAddToActiveCycle(boolean addToActiveCycle) {
        this.addToActiveCycle = addToActiveCycle;
    }
}
