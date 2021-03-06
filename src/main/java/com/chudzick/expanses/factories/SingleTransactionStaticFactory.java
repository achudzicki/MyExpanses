package com.chudzick.expanses.factories;

import com.chudzick.expanses.domain.expanses.Cycle;
import com.chudzick.expanses.domain.expanses.SingleTransaction;
import com.chudzick.expanses.domain.expanses.TransactionDuration;
import com.chudzick.expanses.domain.expanses.dto.SingleTransactionDto;
import com.chudzick.expanses.domain.users.AppUser;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class SingleTransactionStaticFactory {
    private static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static SingleTransaction createFromDto(SingleTransactionDto singleTransactionDto, AppUser appUser, Cycle cycle) {
        SingleTransaction singleTransaction = new SingleTransaction();
        singleTransaction.setAmount(singleTransactionDto.getAmount());
        singleTransaction.setAppUser(appUser);
        singleTransaction.setTransactionDate(LocalDate.parse(singleTransactionDto.getTransactionDate(), dateTimeFormatter));
        singleTransaction.setTransactionGroup(singleTransactionDto.getTransactionGroup());
        singleTransaction.setTransactionType(singleTransactionDto.getTransactionType());
        singleTransaction.setCycle(singleTransactionDto.getCycle() == null ? cycle : singleTransactionDto.getCycle());
        singleTransaction.setTransactionDuration(TransactionDuration.SINGLE);

        return singleTransaction;
    }

}
