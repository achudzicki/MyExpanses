package com.chudzick.expanses.factories;

import com.chudzick.expanses.domain.expanses.SingleTransaction;
import com.chudzick.expanses.domain.expanses.SingleTransactionDto;
import com.chudzick.expanses.domain.users.AppUser;

public class SingleTransactionStaticFactory {

    public static SingleTransaction createFromDto(SingleTransactionDto singleTransactionDto, AppUser appUser) {
        SingleTransaction singleTransaction = new SingleTransaction();

        singleTransaction.setAmound(singleTransactionDto.getAmound());
        singleTransaction.setAppUser(appUser);
        singleTransaction.setTransactionDate(singleTransactionDto.getTransactionDate());
        singleTransaction.setTransactionGroup(singleTransactionDto.getTransactionGroup());
        singleTransaction.setTransactionType(singleTransactionDto.getTransactionType());

        return singleTransaction;
    }

}
