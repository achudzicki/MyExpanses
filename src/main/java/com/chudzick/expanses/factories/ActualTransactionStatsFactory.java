package com.chudzick.expanses.factories;

import com.chudzick.expanses.domain.expanses.Cycle;
import com.chudzick.expanses.domain.expanses.TransactionDuration;
import com.chudzick.expanses.domain.expanses.TransactionType;
import com.chudzick.expanses.domain.expanses.UserTransactions;
import com.chudzick.expanses.domain.statictics.ActualTransactionStats;
import com.chudzick.expanses.domain.statictics.ActualTransactionStatsDto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class ActualTransactionStatsFactory {

    /*RETURN STATISTICS ONLY FOR SELECTED TYPE OF TRANSACTION -> CONSTANT OR SINGLE*/
    public ActualTransactionStats fromTransactionList(List<UserTransactions> allTransactions, TransactionDuration duration, Optional<Cycle> cycle) {
        return prepareStatFromList(allTransactions
                .stream()
                .filter(userTransaction -> userTransaction.getTransactionDuration().equals(duration))
                .collect(Collectors.toList()), cycle);
    }

    /*RETURN STATISTIC OF BOTH TYPES OF TRANSACTIONS SINGLE AND CONSTANT AS UNION OF STATISTIC*/
    public ActualTransactionStats fromTransactionList(List<UserTransactions> allTransactions, Optional<Cycle> cycle) {
        return prepareStatFromList(allTransactions,cycle);
    }

    private ActualTransactionStats prepareStatFromList(List<UserTransactions> allTransactions,Optional<Cycle> activeCycle) {
        ActualTransactionStatsDto stats = new ActualTransactionStatsDto();
        allTransactions
                .forEach(transaction -> {
                    if (transaction.getTransactionType().equals(TransactionType.INCOME)) {
                        stats.incrementIncomeCnt();
                        stats.updateIncomeSum(transaction.getAmount());
                        stats.updateBalance(transaction.getAmount());
                    } else {
                        stats.incrementExpensesCnt();
                        stats.updateExpensesSum(transaction.getAmount());
                        stats.updateBalance(transaction.getAmount().negate());
                    }
                });
        activeCycle.ifPresent(cycle -> stats.updateBalance(cycle.getSaveGoal().negate()));
        return ActualTransactionStats.fromDto(stats);
    }

    public Map<String, ActualTransactionStats> combineTransactionsFromList(List<UserTransactions> allTransactions) {
        Map<TransactionDuration, ActualTransactionStatsDto> combineMap = new HashMap<>();
        combineMap.put(TransactionDuration.CONSTANT, new ActualTransactionStatsDto());
        combineMap.put(TransactionDuration.SINGLE, new ActualTransactionStatsDto());

        allTransactions.forEach(transaction -> {
                    if (transaction.getTransactionType().equals(TransactionType.INCOME)) {
                        ActualTransactionStatsDto stats = transaction.isConstantTransaction() ? combineMap.get(TransactionDuration.CONSTANT) : combineMap.get(TransactionDuration.SINGLE);
                        stats.incrementIncomeCnt();
                        stats.updateIncomeSum(transaction.getAmount());
                        stats.updateBalance(transaction.getAmount());
                        combineMap.put(transaction.getTransactionDuration(), stats);
                    } else {
                        ActualTransactionStatsDto stats = transaction.isConstantTransaction() ? combineMap.get(TransactionDuration.CONSTANT) : combineMap.get(TransactionDuration.SINGLE);
                        stats.incrementExpensesCnt();
                        stats.updateExpensesSum(transaction.getAmount());
                        stats.updateBalance(transaction.getAmount().negate());
                        combineMap.put(transaction.getTransactionDuration(), stats);
                    }
                }
        );
        return combineMap.entrySet()
                .stream().collect(Collectors.toMap(entry -> entry.getKey().toString(),
                        entry -> ActualTransactionStats.fromDto(entry.getValue())));
    }
}
