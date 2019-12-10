package com.chudzick.expanses.domain.savings.dto;

import com.chudzick.expanses.domain.savings.SavingPaymentType;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class SavingPaymentDto {
    @NotNull
    @DecimalMin(value = "0.00", message = "Kwota nie może być mniejsza od zera")
    private BigDecimal amount;
    @NotNull
    private SavingPaymentType type;
    private BigDecimal userPaymentsSum;

    public BigDecimal getUserPaymentsSum() {
        return userPaymentsSum;
    }

    public void setUserPaymentsSum(BigDecimal userPaymentsSum) {
        this.userPaymentsSum = userPaymentsSum;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public SavingPaymentType getType() {
        return type;
    }

    public void setType(SavingPaymentType type) {
        this.type = type;
    }
}
