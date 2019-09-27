package com.chudzick.expanses.factories;

import com.chudzick.expanses.domain.expanses.TransactionDuration;
import com.chudzick.expanses.domain.expanses.TransactionType;
import com.chudzick.expanses.domain.expanses.UserTransactions;
import com.chudzick.expanses.domain.statictics.ActualTransactionStats;
import com.chudzick.expanses.domain.statictics.ActualTransactionStatsDto;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ActualTransactionStatsFactory {

    public ActualTransactionStats fromTransactionList(List<UserTransactions> allTransactions, TransactionDuration duration) {
        ActualTransactionStatsDto stats = new ActualTransactionStatsDto();
        allTransactions
                .stream()
                .filter(userTransaction -> userTransaction.getTransactionDuration().equals(duration))
                .forEach(transaction -> {
                    if (transaction.getTransactionType().equals(TransactionType.INCOME)) {
                        stats.incrementIncomeCnt();
                        stats.updateIncomeSum(transaction.getAmound());
                        stats.updateBalance(transaction.getAmound());
                    } else {
                        stats.incrementExpensesCnt();
                        stats.updateExpensesSum(transaction.getAmound());
                        stats.updateBalance(transaction.getAmound());
                    }
                });
        return ActualTransactionStats.fromDto(stats);
    }

    public Map<TransactionDuration, ActualTransactionStats> combineTransactionsFromList(List<UserTransactions> allTransactions) {
        Map<TransactionDuration, ActualTransactionStatsDto> combineMap = new EnumMap<>(TransactionDuration.class);
        combineMap.put(TransactionDuration.CONSTANT, new ActualTransactionStatsDto());
        combineMap.put(TransactionDuration.SINGLE, new ActualTransactionStatsDto());

        allTransactions.forEach(transaction -> {
                    if (transaction.getTransactionType().equals(TransactionType.INCOME)) {
                        ActualTransactionStatsDto stats = transaction.isConstantTransaction() ? combineMap.get(TransactionDuration.CONSTANT) : combineMap.get(TransactionDuration.SINGLE);
                        stats.incrementIncomeCnt();
                        stats.updateIncomeSum(transaction.getAmound());
                        stats.updateBalance(transaction.getAmound());
                        combineMap.put(transaction.getTransactionDuration(),stats);
                    } else {
                        ActualTransactionStatsDto stats = transaction.isConstantTransaction() ? combineMap.get(TransactionDuration.CONSTANT) : combineMap.get(TransactionDuration.SINGLE);
                        stats.incrementExpensesCnt();
                        stats.updateExpensesSum(transaction.getAmound());
                        stats.updateBalance(transaction.getAmound());
                        combineMap.put(transaction.getTransactionDuration(),stats);
                    }
                }
        );
        return combineMap.entrySet().contains(Collectors.toMap(Map.Entry::getKey, k-> ActualTransactionStats.fromDto(k)));
    }
}
