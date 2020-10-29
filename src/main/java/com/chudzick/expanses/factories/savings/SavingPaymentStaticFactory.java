package com.chudzick.expanses.factories.savings;

import com.chudzick.expanses.domain.savings.SavingGoal;
import com.chudzick.expanses.domain.savings.SavingPayment;
import com.chudzick.expanses.domain.savings.dto.SavingPaymentDto;
import com.chudzick.expanses.domain.users.AppUser;

import java.time.LocalDate;
import java.util.Optional;

public class SavingPaymentStaticFactory {

    public static Optional<SavingPayment> createFromDto(SavingPaymentDto savingPaymentDto, AppUser appUser, SavingGoal goal) {
        if (savingPaymentDto == null || appUser == null || goal == null) {
            return Optional.empty();
        }
        SavingPayment savingPayment = new SavingPayment();
        savingPayment.setAmount(savingPaymentDto.getAmount());
        savingPayment.setSavingPaymentType(savingPaymentDto.getType());
        savingPayment.setAppUser(appUser);
        savingPayment.setSavingGoal(goal);
        savingPayment.setPaymentDate(LocalDate.now());
        return Optional.of(savingPayment);
    }

}
