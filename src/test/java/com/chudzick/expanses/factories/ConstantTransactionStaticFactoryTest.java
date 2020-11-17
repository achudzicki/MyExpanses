package com.chudzick.expanses.factories;

import com.chudzick.expanses.TestTransactionGroupsSupplier;
import com.chudzick.expanses.domain.expanses.ConstantTransaction;
import com.chudzick.expanses.domain.expanses.Cycle;
import com.chudzick.expanses.domain.expanses.TransactionDuration;
import com.chudzick.expanses.domain.expanses.TransactionType;
import com.chudzick.expanses.domain.expanses.dto.ConstantTransactionDto;
import com.chudzick.expanses.domain.users.AppUser;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;

public class ConstantTransactionStaticFactoryTest implements TestTransactionGroupsSupplier {
    private static final AppUser APP_USER = new AppUser();
    private static final boolean ADD_TO_ACTIVE_CYCLE = true;
    private static final BigDecimal AMOUNT = new BigDecimal(1);
    private static final int CYCLES_COUNT = 10;
    private static final boolean PERMANENT_DURATION = false;
    private static final TransactionType TRANSACTION_TYPE = TransactionType.INCOME;
    private static final Cycle CYCLE = new Cycle();

    @Test
    public void creatFromDto_PassFullDataAsLimitedTransactionAndCycle_ReturnConstantTransaction() {
        ConstantTransactionDto dto = prepareDto();

        ConstantTransaction constantTransaction = ConstantTransactionStaticFactory.fromDto(dto, APP_USER, CYCLE);

        Assert.assertNotNull(constantTransaction);
        Assert.assertNotNull(constantTransaction.getCycles());
        Assert.assertEquals(1,constantTransaction.getCycles().size());
        Assert.assertEquals(1, constantTransaction.getCyclesAppears(), 0);
        Assert.assertEquals(10, constantTransaction.getCyclesCount(), 0);
        Assert.assertEquals(AMOUNT, constantTransaction.getAmount());
        Assert.assertEquals(TRANSACTION_TYPE, constantTransaction.getTransactionType());
        Assert.assertEquals(prepareTransactionGroup(APP_USER), constantTransaction.getTransactionGroup());
        Assert.assertEquals(TransactionDuration.CONSTANT, constantTransaction.getTransactionDuration());
        Assert.assertEquals(APP_USER, constantTransaction.getAppUser());
    }

    @Test
    public void creatFromDto_PassFullDataAsNotLimitedTransactionAncCycle_ReturnConstantTransaction() {
        ConstantTransactionDto dto = prepareDto();
        dto.setPermanentDuration(true);

        ConstantTransaction constantTransaction = ConstantTransactionStaticFactory.fromDto(dto, APP_USER, CYCLE);

        Assert.assertNotNull(constantTransaction);
        Assert.assertNotNull(constantTransaction.getCycles());
        Assert.assertEquals(1,constantTransaction.getCycles().size());
        Assert.assertNull(constantTransaction.getCyclesAppears());
        Assert.assertNull(constantTransaction.getCyclesCount());
        Assert.assertEquals(AMOUNT, constantTransaction.getAmount());
        Assert.assertEquals(TRANSACTION_TYPE, constantTransaction.getTransactionType());
        Assert.assertEquals(prepareTransactionGroup(APP_USER), constantTransaction.getTransactionGroup());
        Assert.assertEquals(TransactionDuration.CONSTANT, constantTransaction.getTransactionDuration());
        Assert.assertEquals(APP_USER, constantTransaction.getAppUser());
    }

    @Test
    public void creatFromDto_PassFullDataAsLimitedTransaction_ReturnConstantTransaction() {
        ConstantTransactionDto dto = prepareDto();

        ConstantTransaction constantTransaction = ConstantTransactionStaticFactory.fromDto(dto, APP_USER);

        Assert.assertNotNull(constantTransaction);
        Assert.assertNull(constantTransaction.getCycles());
        Assert.assertEquals(1, constantTransaction.getCyclesAppears(), 0);
        Assert.assertEquals(10, constantTransaction.getCyclesCount(), 0);
        Assert.assertNull(constantTransaction.getCycles());
        Assert.assertEquals(AMOUNT, constantTransaction.getAmount());
        Assert.assertEquals(TRANSACTION_TYPE, constantTransaction.getTransactionType());
        Assert.assertEquals(prepareTransactionGroup(APP_USER), constantTransaction.getTransactionGroup());
        Assert.assertEquals(TransactionDuration.CONSTANT, constantTransaction.getTransactionDuration());
        Assert.assertEquals(APP_USER, constantTransaction.getAppUser());
    }

    @Test
    public void creatFromDto_PassFullDataAsNotLimitedTransaction_ReturnConstantTransaction() {
        ConstantTransactionDto dto = prepareDto();
        dto.setPermanentDuration(true);

        ConstantTransaction constantTransaction = ConstantTransactionStaticFactory.fromDto(dto, APP_USER);

        Assert.assertNotNull(constantTransaction);
        Assert.assertNull(constantTransaction.getCycles());
        Assert.assertNull(constantTransaction.getCyclesAppears());
        Assert.assertNull(constantTransaction.getCyclesCount());
        Assert.assertEquals(AMOUNT, constantTransaction.getAmount());
        Assert.assertEquals(TRANSACTION_TYPE, constantTransaction.getTransactionType());
        Assert.assertEquals(prepareTransactionGroup(APP_USER), constantTransaction.getTransactionGroup());
        Assert.assertEquals(TransactionDuration.CONSTANT, constantTransaction.getTransactionDuration());
        Assert.assertEquals(APP_USER, constantTransaction.getAppUser());
    }

    private ConstantTransactionDto prepareDto() {
        ConstantTransactionDto dto = new ConstantTransactionDto();
        dto.setAddToActiveCycle(ADD_TO_ACTIVE_CYCLE);
        dto.setAmount(AMOUNT);
        dto.setCyclesCount(CYCLES_COUNT);
        dto.setPermanentDuration(PERMANENT_DURATION);
        dto.setTransactionGroup(prepareTransactionGroup(APP_USER));
        dto.setTransactionType(TRANSACTION_TYPE);
        return dto;
    }

}