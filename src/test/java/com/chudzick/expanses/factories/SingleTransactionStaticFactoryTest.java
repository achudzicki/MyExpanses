package com.chudzick.expanses.factories;

import com.chudzick.expanses.TestSingleTransactionSupplier;
import com.chudzick.expanses.TestTransactionGroupsSupplier;
import com.chudzick.expanses.domain.expanses.Cycle;
import com.chudzick.expanses.domain.expanses.SingleTransaction;
import com.chudzick.expanses.domain.expanses.dto.SingleTransactionDto;
import com.chudzick.expanses.domain.users.AppUser;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class SingleTransactionStaticFactoryTest implements TestTransactionGroupsSupplier, TestSingleTransactionSupplier {
    private static final AppUser APP_USER = new AppUser();
    private static final Cycle CYCLE = new Cycle();
    private static DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Test
    public void createFromDto_PassFullData_ReturnSingleTransaction() {
        SingleTransactionDto dto = prepareValidSingleTransactionDto(prepareTransactionGroup(APP_USER));

        SingleTransaction singleTransaction = SingleTransactionStaticFactory.createFromDto(dto, APP_USER, CYCLE);

        Assert.assertNotNull(singleTransaction);
        Assert.assertEquals(LocalDate.parse(dto.getTransactionDate(), DATE_TIME_FORMATTER), singleTransaction.getTransactionDate());
        Assert.assertEquals(CYCLE, singleTransaction.getCycle());
        Assert.assertEquals(dto.getAmount(), singleTransaction.getAmount());
        Assert.assertEquals(dto.getTransactionType(), singleTransaction.getTransactionType());
        Assert.assertEquals(prepareTransactionGroup(APP_USER), singleTransaction.getTransactionGroup());
        Assert.assertEquals(APP_USER, singleTransaction.getAppUser());
    }
}