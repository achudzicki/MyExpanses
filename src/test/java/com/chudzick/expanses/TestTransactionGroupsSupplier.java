package com.chudzick.expanses;

import com.chudzick.expanses.domain.expanses.TransactionGroup;
import com.chudzick.expanses.domain.expanses.dto.TransactionGroupDto;
import com.chudzick.expanses.domain.users.AppUser;

public interface TestTransactionGroupsSupplier {
    String TEST_DESCRIPTION = "TEST_DESCRIPTION";
    String TEST_GROUP_NAME = "TEST_GROUP_NAME";

    default TransactionGroup prepareTransactionGroup(AppUser appUser) {
        TransactionGroup transactionGroup = new TransactionGroup();

        transactionGroup.setAppUser(appUser);
        transactionGroup.setGroupDescription(TEST_DESCRIPTION);
        transactionGroup.setGorupName(TEST_GROUP_NAME);

        return transactionGroup;
    }

    default TransactionGroupDto prepareValidTransactionGroupDto() {
        TransactionGroupDto transactionGroupDto = new TransactionGroupDto();

        transactionGroupDto.setGorupName(TEST_GROUP_NAME);
        transactionGroupDto.setGroupDescription(TEST_DESCRIPTION);

        return transactionGroupDto;
    }
}
