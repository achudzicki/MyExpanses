package com.chudzick.expanses.factories;

import com.chudzick.expanses.TestUserSupplier;
import com.chudzick.expanses.domain.expanses.TransactionGroup;
import com.chudzick.expanses.domain.expanses.dto.TransactionGroupDto;
import com.chudzick.expanses.domain.users.AppUser;
import org.junit.Assert;
import org.junit.Test;

public class TransactionGroupStaticFactoryTest implements TestUserSupplier {

    @Test
    public void createFromDtoTest() {
        TransactionGroupDto transactionGroupDto = new TransactionGroupDto();
        transactionGroupDto.setGroupDescription("testDesc");
        transactionGroupDto.setGorupName("testName");

        AppUser currentUserMock = AppUserStaticFactory.createFromDto(prepareValidUserDto());

        TransactionGroup transactionGroup = TransactionGroupStaticFactory.createFromDto(transactionGroupDto, currentUserMock);

        Assert.assertEquals(transactionGroup.getAppUser(), currentUserMock);
        Assert.assertEquals(transactionGroupDto.getGorupName(),transactionGroup.getGorupName());
        Assert.assertEquals(transactionGroupDto.getGroupDescription(),transactionGroup.getGroupDescription());
    }
}
