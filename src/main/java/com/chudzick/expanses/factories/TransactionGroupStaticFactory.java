package com.chudzick.expanses.factories;

import com.chudzick.expanses.domain.expanses.TransactionGroup;
import com.chudzick.expanses.domain.expanses.dto.TransactionGroupDto;
import com.chudzick.expanses.domain.users.AppUser;

public class TransactionGroupStaticFactory {

    public static TransactionGroup createFromDto(TransactionGroupDto transactionGroupDto, AppUser appUser) {
        TransactionGroup transactionGroup = new TransactionGroup();

        transactionGroup.setGroupDescription(transactionGroupDto.getGroupDescription());
        transactionGroup.setGorupName(transactionGroupDto.getGorupName());
        transactionGroup.setAppUser(appUser);

        return transactionGroup;
    }
}
