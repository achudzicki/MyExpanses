package com.chudzick.expanses.factories.savings;

import com.chudzick.expanses.domain.savings.SavingGoal;
import com.chudzick.expanses.domain.savings.SavingPayment;
import com.chudzick.expanses.domain.savings.SavingPaymentType;
import com.chudzick.expanses.domain.savings.dto.SavingPaymentDto;
import com.chudzick.expanses.domain.users.AppUser;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Optional;

public class SavingPaymentStaticFactoryTest {
    private static final AppUser APP_USER = new AppUser();
    private static final BigDecimal AMOUNT = new BigDecimal(1);
    private static final SavingPaymentType SAVING_PAYMENT_TYPE = SavingPaymentType.ADD;

    @Test
    public void createFromDto_PassAllData_ReturnValidSavingPayment() {
        SavingGoal goal = prepareSavingGoal();
        Optional<SavingPayment> savingPayment = SavingPaymentStaticFactory.createFromDto(prepareDto(), APP_USER, goal);

        Assert.assertTrue(savingPayment.isPresent());
        Assert.assertEquals(AMOUNT, savingPayment.get().getAmount());
        Assert.assertEquals(APP_USER, savingPayment.get().getAppUser());
        Assert.assertEquals(SAVING_PAYMENT_TYPE, savingPayment.get().getSavingPaymentType());
        Assert.assertEquals(goal, savingPayment.get().getSavingGoal());
    }

    @Test
    public void createFromDto_PassDataWithNullAppUser_ReturnOptionalEmpty() {
        SavingGoal goal = prepareSavingGoal();
        Optional<SavingPayment> savingPayment = SavingPaymentStaticFactory.createFromDto(prepareDto(), null, goal);

        Assert.assertFalse(savingPayment.isPresent());
    }

    @Test
    public void createFromDto_PassDataWithNullDto_ReturnOptionalEmpty() {
        SavingGoal goal = prepareSavingGoal();
        Optional<SavingPayment> savingPayment = SavingPaymentStaticFactory.createFromDto(null, APP_USER, goal);

        Assert.assertFalse(savingPayment.isPresent());
    }

    @Test
    public void createFromDto_PassDataWithNullGoal_ReturnOptionalEmpty() {
        Optional<SavingPayment> savingPayment = SavingPaymentStaticFactory.createFromDto(prepareDto(), APP_USER, null);

        Assert.assertFalse(savingPayment.isPresent());
    }

    private SavingPaymentDto prepareDto() {
        SavingPaymentDto dto = new SavingPaymentDto();
        dto.setAmount(AMOUNT);
        dto.setType(SAVING_PAYMENT_TYPE);
        dto.setUserPaymentsSum(new BigDecimal(1));
        return dto;
    }

    private SavingGoal prepareSavingGoal() {
        return new SavingGoal();
    }
}