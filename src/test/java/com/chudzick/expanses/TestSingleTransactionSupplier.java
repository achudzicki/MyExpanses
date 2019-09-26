package com.chudzick.expanses;

import com.chudzick.expanses.domain.expanses.*;
import com.chudzick.expanses.domain.users.AppUser;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface TestSingleTransactionSupplier {

    default SingleTransaction prepareSingleTransaction(Cycle cycle, AppUser appUser) {
        SingleTransaction singleTransaction = new SingleTransaction();

        singleTransaction.setCycle(cycle);
        singleTransaction.setAppUser(appUser);
        singleTransaction.setTransactionType(TransactionType.INCOME);
        singleTransaction.setAmound(BigDecimal.ONE);
        singleTransaction.setTransactionDate(LocalDate.now());
        singleTransaction.setId(1L);

        return singleTransaction;
    }

    default SingleTransactionDto prepareValidSingleTransactionDto(TransactionGroup transactionGroup) {
        SingleTransactionDto singleTransactionDto = new SingleTransactionDto();

        singleTransactionDto.setAmount(BigDecimal.ONE);
        singleTransactionDto.setTransactionDate("2019-01-01");
        singleTransactionDto.setTransactionType(TransactionType.INCOME);
        singleTransactionDto.setTransactionGroup(transactionGroup);

        return singleTransactionDto;
    }
}
