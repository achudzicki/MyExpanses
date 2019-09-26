package com.chudzick.expanses.factories;

import com.chudzick.expanses.domain.expanses.ConstantTransaction;
import com.chudzick.expanses.domain.expanses.Cycle;
import com.chudzick.expanses.domain.expanses.TransactionDuration;
import com.chudzick.expanses.domain.expanses.dto.ConstantTransactionDto;
import com.chudzick.expanses.domain.users.AppUser;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ConstantTransactionStaticFactory {

    public static ConstantTransaction fromDto(ConstantTransactionDto constantTransactionDto, AppUser appUser) {
        return setUpBasicInfo(constantTransactionDto, appUser);
    }

    public static ConstantTransaction fromDto(ConstantTransactionDto constantTransactionDto, AppUser appUser, Cycle cycle) {
        ConstantTransaction constantTransaction = setUpBasicInfo(constantTransactionDto, appUser);
        constantTransaction.setCycles(Stream.of(cycle).collect(Collectors.toSet()));
        return constantTransaction;
    }

    private static ConstantTransaction setUpBasicInfo(ConstantTransactionDto constantTransactionDto, AppUser appUser) {
        ConstantTransaction constantTransaction = new ConstantTransaction();

        constantTransaction.setTransactionType(constantTransactionDto.getTransactionType());
        constantTransaction.setTransactionGroup(constantTransactionDto.getTransactionGroup());
        constantTransaction.setAppUser(appUser);
        constantTransaction.setPermanentDuration(constantTransactionDto.isPermanentDuration());
        constantTransaction.setAmound(constantTransactionDto.getAmound());
        constantTransaction.setActive(true);
        constantTransaction.setTransactionDuration(TransactionDuration.CONSTANT);

        if (!constantTransactionDto.isPermanentDuration()) {
            constantTransaction.setCyclesAppears(constantTransactionDto.getCyclesAppears());
            constantTransaction.setCyclesCount(constantTransaction.getCyclesCount());
        }
        return constantTransaction;
    }
}