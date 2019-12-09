package com.chudzick.expanses.domain.savings.dto;

import com.chudzick.expanses.domain.paging.PageSettings;
import com.chudzick.expanses.domain.paging.RequestPage;
import com.chudzick.expanses.domain.savings.SavingGoal;
import com.chudzick.expanses.domain.savings.SavingPayment;
import com.chudzick.expanses.factories.paging.PageFactory;

import java.math.BigDecimal;

public class SavingGoalView {
    private SavingGoal savingGoal;
    private RequestPage<SavingPayment> paymentPage;
    private BigDecimal userPaymentsSum;

    public SavingGoalView(SavingGoal savingGoal, BigDecimal userPaymentsSum) {
        this.savingGoal = savingGoal;
        this.userPaymentsSum = userPaymentsSum;
        paymentPage = new PageFactory<SavingPayment>()
                .getRequestPage(savingGoal.getSavingPayments(), PageSettings.FIRST_PAGE.getValue(), PageSettings.SMALL_PAGE_CONTENT_SIZE.getValue());
    }

    public BigDecimal getUserPaymentsSum() {
        return userPaymentsSum;
    }

    public void setUserPaymentsSum(BigDecimal userPaymentsSum) {
        this.userPaymentsSum = userPaymentsSum;
    }

    public SavingGoal getSavingGoal() {
        return savingGoal;
    }

    public void setSavingGoal(SavingGoal savingGoal) {
        this.savingGoal = savingGoal;
    }

    public RequestPage<SavingPayment> getPaymentPage() {
        return paymentPage;
    }

    public void setPaymentPage(RequestPage<SavingPayment> paymentPage) {
        this.paymentPage = paymentPage;
    }
}
