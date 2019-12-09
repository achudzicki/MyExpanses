package com.chudzick.expanses.beans.savings;

import com.chudzick.expanses.domain.savings.dto.SavingGoalView;

import java.math.BigDecimal;
import java.util.List;

public class SavingGoalBean {
    private BigDecimal savingSum;
    private BigDecimal savingToAllocate;
    private BigDecimal userPaymentsSum;
    private List<SavingGoalView> userSavingGoals;

    public BigDecimal getUserPaymentsSum() {
        return userPaymentsSum;
    }

    public void setUserPaymentsSum(BigDecimal userPaymentsSum) {
        this.userPaymentsSum = userPaymentsSum;
    }

    public BigDecimal getSavingSum() {
        return savingSum;
    }

    public void setSavingSum(BigDecimal savingSum) {
        this.savingSum = savingSum;
    }

    public BigDecimal getSavingToAllocate() {
        return savingToAllocate;
    }

    public void setSavingToAllocate(BigDecimal savingToAllocate) {
        this.savingToAllocate = savingToAllocate;
    }

    public List<SavingGoalView> getUserSavingGoals() {
        return userSavingGoals;
    }

    public void setUserSavingGoals(List<SavingGoalView> userSavingGoals) {
        this.userSavingGoals = userSavingGoals;
    }
}
