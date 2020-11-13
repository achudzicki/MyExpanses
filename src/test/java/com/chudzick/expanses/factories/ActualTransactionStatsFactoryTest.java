package com.chudzick.expanses.factories;

import com.chudzick.expanses.domain.expanses.*;
import com.chudzick.expanses.domain.statictics.ActualTransactionStats;
import com.chudzick.expanses.domain.users.AppUser;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.*;

public class ActualTransactionStatsFactoryTest {
    private static final AppUser APP_USER = new AppUser();
    private static final long ID = 1;
    private static final BigDecimal AMOUNT = new BigDecimal(10);
    private static final TransactionGroup TRANSACTION_GROUP = new TransactionGroup();
    private static final ActualTransactionStatsFactory actualTransactionStatsFactory = new ActualTransactionStatsFactory();

    @Test
    public void fromTransactionList_PassListWith10Transactions5SingleType3Income2ExpansesAndTypeSingleAndNoActiveCycle_ReturnStats30Income20Expanse() {
        List<UserTransactions> userTransactions = new ArrayList<>();
        int expanseCnt = 2;
        int incomeCnt = 3;
        BigDecimal balance = AMOUNT.multiply(new BigDecimal(incomeCnt)).subtract(AMOUNT.multiply(new BigDecimal(expanseCnt)));

        for (int i = 0; i < incomeCnt; i++) {
            userTransactions.add(prepareTransaction(TransactionDuration.SINGLE, TransactionType.INCOME));
        }
        for (int i = 0; i < expanseCnt; i++) {
            userTransactions.add(prepareTransaction(TransactionDuration.SINGLE, TransactionType.EXPANSE));
        }
        for (int i = 0; i < 5; i++) {
            userTransactions.add(prepareTransaction(TransactionDuration.CONSTANT, TransactionType.INCOME));
        }

        ActualTransactionStats ststs = actualTransactionStatsFactory.fromTransactionList(userTransactions, TransactionDuration.SINGLE, Optional.empty());

        Assert.assertNotNull(ststs);
        Assert.assertEquals(expanseCnt, ststs.getExpensesCnt());
        Assert.assertEquals(incomeCnt, ststs.getIncomeCnt());
        Assert.assertEquals(AMOUNT.multiply(new BigDecimal(expanseCnt)), ststs.getExpensesSum());
        Assert.assertEquals(AMOUNT.multiply(new BigDecimal(incomeCnt)), ststs.getIncomeSum());
        Assert.assertEquals(balance, ststs.getBalance());
    }

    @Test
    public void fromTransactionList_PassListWith10Transactions5SingleType3Income2ExpansesAndTypeSingleAndCycleWithSaveGoal10_ReturnStats30Income20Expanse() {
        List<UserTransactions> userTransactions = new ArrayList<>();
        Cycle cycle = prepareCycle();
        int expanseCnt = 2;
        int incomeCnt = 3;
        BigDecimal balance = (AMOUNT.multiply(new BigDecimal(incomeCnt)).subtract(AMOUNT.multiply(new BigDecimal(expanseCnt)))).subtract(cycle.getSaveGoal());

        for (int i = 0; i < incomeCnt; i++) {
            userTransactions.add(prepareTransaction(TransactionDuration.SINGLE, TransactionType.INCOME));
        }
        for (int i = 0; i < expanseCnt; i++) {
            userTransactions.add(prepareTransaction(TransactionDuration.SINGLE, TransactionType.EXPANSE));
        }
        for (int i = 0; i < 5; i++) {
            userTransactions.add(prepareTransaction(TransactionDuration.CONSTANT, TransactionType.INCOME));
        }

        ActualTransactionStats ststs = actualTransactionStatsFactory.fromTransactionList(userTransactions, TransactionDuration.SINGLE, Optional.of(cycle));

        Assert.assertNotNull(ststs);
        Assert.assertEquals(expanseCnt, ststs.getExpensesCnt());
        Assert.assertEquals(incomeCnt, ststs.getIncomeCnt());
        Assert.assertEquals(AMOUNT.multiply(new BigDecimal(expanseCnt)), ststs.getExpensesSum());
        Assert.assertEquals(AMOUNT.multiply(new BigDecimal(incomeCnt)), ststs.getIncomeSum());
        Assert.assertEquals(balance, ststs.getBalance());
    }

    @Test
    public void fromTransactionList_PassListWith10Transactions5ConstantType5IncomeAndTypeConstantAndNoActiveCycle_ReturnStats30Income20Expanse() {
        List<UserTransactions> userTransactions = new ArrayList<>();
        int cnt = 5;
        BigDecimal balance = AMOUNT.multiply(new BigDecimal(cnt));

        for (int i = 0; i < 4; i++) {
            userTransactions.add(prepareTransaction(TransactionDuration.SINGLE, TransactionType.INCOME));
        }
        for (int i = 0; i < 1; i++) {
            userTransactions.add(prepareTransaction(TransactionDuration.SINGLE, TransactionType.EXPANSE));
        }
        for (int i = 0; i < cnt; i++) {
            userTransactions.add(prepareTransaction(TransactionDuration.CONSTANT, TransactionType.INCOME));
        }

        ActualTransactionStats ststs = actualTransactionStatsFactory.fromTransactionList(userTransactions, TransactionDuration.CONSTANT, Optional.empty());

        Assert.assertNotNull(ststs);
        Assert.assertEquals(0, ststs.getExpensesCnt());
        Assert.assertEquals(cnt, ststs.getIncomeCnt());
        Assert.assertEquals(BigDecimal.ZERO, ststs.getExpensesSum());
        Assert.assertEquals(AMOUNT.multiply(new BigDecimal(cnt)), ststs.getIncomeSum());
        Assert.assertEquals(balance, ststs.getBalance());
    }

    @Test
    public void fromTransactionList_PassEmptyListAndNoCycle_ReturnEmptyStats() {
        List<UserTransactions> userTransactions = Collections.emptyList();
        BigDecimal balance = BigDecimal.ZERO;

        ActualTransactionStats ststs = actualTransactionStatsFactory.fromTransactionList(userTransactions, TransactionDuration.SINGLE, Optional.empty());

        Assert.assertNotNull(ststs);
        Assert.assertEquals(0, ststs.getExpensesCnt());
        Assert.assertEquals(0, ststs.getIncomeCnt());
        Assert.assertEquals(BigDecimal.ZERO, ststs.getExpensesSum());
        Assert.assertEquals(BigDecimal.ZERO, ststs.getIncomeSum());
        Assert.assertEquals(balance, ststs.getBalance());
    }

    @Test
    public void fromTransactionList_PassEmptyListAndNoCycleWithSaveGoal10_ReturnEmptyStats() {
        List<UserTransactions> userTransactions = Collections.emptyList();
        Cycle cycle = prepareCycle();
        BigDecimal balance = BigDecimal.ZERO.subtract(cycle.getSaveGoal());

        ActualTransactionStats ststs = actualTransactionStatsFactory.fromTransactionList(userTransactions, TransactionDuration.SINGLE, Optional.of(cycle));

        Assert.assertNotNull(ststs);
        Assert.assertEquals(0, ststs.getExpensesCnt());
        Assert.assertEquals(0, ststs.getIncomeCnt());
        Assert.assertEquals(BigDecimal.ZERO, ststs.getExpensesSum());
        Assert.assertEquals(BigDecimal.ZERO, ststs.getIncomeSum());
        Assert.assertEquals(balance, ststs.getBalance());
    }

    @Test
    public void fromTransactionList_PassNullListAndNoCycle_ReturnEmptyStats() {
        BigDecimal balance = BigDecimal.ZERO;

        ActualTransactionStats ststs = actualTransactionStatsFactory.fromTransactionList(null, TransactionDuration.SINGLE, Optional.empty());

        Assert.assertNotNull(ststs);
        Assert.assertEquals(0, ststs.getExpensesCnt());
        Assert.assertEquals(0, ststs.getIncomeCnt());
        Assert.assertEquals(BigDecimal.ZERO, ststs.getExpensesSum());
        Assert.assertEquals(BigDecimal.ZERO, ststs.getIncomeSum());
        Assert.assertEquals(balance, ststs.getBalance());
    }


    @Test
    public void fromTransactionList_PassListWith10Transactions8Income2ExpansesAndTypeSingleAndNoActiveCycle_ReturnStats70Income30Expanse() {
        List<UserTransactions> userTransactions = new ArrayList<>();
        int expanseCnt = 2;
        int incomeCnt = 3;
        int constantIncomeCnt = 4;
        int constantExpanseCnt = 1;
        BigDecimal balance = new BigDecimal(40);

        for (int i = 0; i < incomeCnt; i++) {
            userTransactions.add(prepareTransaction(TransactionDuration.SINGLE, TransactionType.INCOME));
        }
        for (int i = 0; i < expanseCnt; i++) {
            userTransactions.add(prepareTransaction(TransactionDuration.SINGLE, TransactionType.EXPANSE));
        }
        for (int i = 0; i < constantIncomeCnt; i++) {
            userTransactions.add(prepareTransaction(TransactionDuration.CONSTANT, TransactionType.INCOME));
        }
        for (int i = 0; i < constantExpanseCnt; i++) {
            userTransactions.add(prepareTransaction(TransactionDuration.CONSTANT, TransactionType.EXPANSE));
        }

        ActualTransactionStats ststs = actualTransactionStatsFactory.fromTransactionList(userTransactions, Optional.empty());

        Assert.assertNotNull(ststs);
        Assert.assertEquals(expanseCnt + constantExpanseCnt, ststs.getExpensesCnt());
        Assert.assertEquals(incomeCnt + constantIncomeCnt, ststs.getIncomeCnt());
        Assert.assertEquals(AMOUNT.multiply(new BigDecimal(expanseCnt)).add(AMOUNT.multiply(new BigDecimal(constantExpanseCnt))), ststs.getExpensesSum());
        Assert.assertEquals(AMOUNT.multiply(new BigDecimal(incomeCnt)).add(AMOUNT.multiply(new BigDecimal(constantIncomeCnt))), ststs.getIncomeSum());
        Assert.assertEquals(balance, ststs.getBalance());
    }

    @Test
    public void fromTransactionList_PassListWith10Transactions8Income2ExpansesAndTypeSingleAndCycleWithSaveGoal10_ReturnStats70Income30Expanse() {
        List<UserTransactions> userTransactions = new ArrayList<>();
        int expanseCnt = 2;
        int incomeCnt = 3;
        int constantIncomeCnt = 4;
        int constantExpanseCnt = 1;
        Cycle cycle = prepareCycle();
        BigDecimal balance = new BigDecimal(30);

        for (int i = 0; i < incomeCnt; i++) {
            userTransactions.add(prepareTransaction(TransactionDuration.SINGLE, TransactionType.INCOME));
        }
        for (int i = 0; i < expanseCnt; i++) {
            userTransactions.add(prepareTransaction(TransactionDuration.SINGLE, TransactionType.EXPANSE));
        }
        for (int i = 0; i < constantIncomeCnt; i++) {
            userTransactions.add(prepareTransaction(TransactionDuration.CONSTANT, TransactionType.INCOME));
        }
        for (int i = 0; i < constantExpanseCnt; i++) {
            userTransactions.add(prepareTransaction(TransactionDuration.CONSTANT, TransactionType.EXPANSE));
        }

        ActualTransactionStats ststs = actualTransactionStatsFactory.fromTransactionList(userTransactions, Optional.of(cycle));

        Assert.assertNotNull(ststs);
        Assert.assertEquals(expanseCnt + constantExpanseCnt, ststs.getExpensesCnt());
        Assert.assertEquals(incomeCnt + constantIncomeCnt, ststs.getIncomeCnt());
        Assert.assertEquals(AMOUNT.multiply(new BigDecimal(expanseCnt)).add(AMOUNT.multiply(new BigDecimal(constantExpanseCnt))), ststs.getExpensesSum());
        Assert.assertEquals(AMOUNT.multiply(new BigDecimal(incomeCnt)).add(AMOUNT.multiply(new BigDecimal(constantIncomeCnt))), ststs.getIncomeSum());
        Assert.assertEquals(balance, ststs.getBalance());
    }

    @Test
    public void fromTransactionList_PassNullListAndNoCycleForAllTransactionDuration_ReturnEmptyStats() {
        BigDecimal balance = BigDecimal.ZERO;

        ActualTransactionStats ststs = actualTransactionStatsFactory.fromTransactionList(null, Optional.empty());

        Assert.assertNotNull(ststs);
        Assert.assertEquals(0, ststs.getExpensesCnt());
        Assert.assertEquals(0, ststs.getIncomeCnt());
        Assert.assertEquals(BigDecimal.ZERO, ststs.getExpensesSum());
        Assert.assertEquals(BigDecimal.ZERO, ststs.getIncomeSum());
        Assert.assertEquals(balance, ststs.getBalance());
    }

    @Test
    public void fromTransactionList_PassNullListAndCycleWithSaveGoal10ForAllTransactionDuration_ReturnEmptyStats() {
        Cycle cycle = prepareCycle();
        BigDecimal balance = new BigDecimal(-10);

        ActualTransactionStats ststs = actualTransactionStatsFactory.fromTransactionList(null, Optional.of(cycle));

        Assert.assertNotNull(ststs);
        Assert.assertEquals(0, ststs.getExpensesCnt());
        Assert.assertEquals(0, ststs.getIncomeCnt());
        Assert.assertEquals(BigDecimal.ZERO, ststs.getExpensesSum());
        Assert.assertEquals(BigDecimal.ZERO, ststs.getIncomeSum());
        Assert.assertEquals(balance, ststs.getBalance());
    }

    @Test
    public void combineTransactionsFromList_PassListWith10Transactions_ReturnMapWithValidIncomesAndExpansesByType() {
        List<UserTransactions> userTransactions = new ArrayList<>();
        int expanseCnt = 2;
        int incomeCnt = 3;
        int constantIncomeCnt = 4;
        int constantExpanseCnt = 1;
        BigDecimal singleBalane = new BigDecimal(10);
        BigDecimal constantBalance = new BigDecimal(30);

        for (int i = 0; i < incomeCnt; i++) {
            userTransactions.add(prepareTransaction(TransactionDuration.SINGLE, TransactionType.INCOME));
        }
        for (int i = 0; i < expanseCnt; i++) {
            userTransactions.add(prepareTransaction(TransactionDuration.SINGLE, TransactionType.EXPANSE));
        }
        for (int i = 0; i < constantIncomeCnt; i++) {
            userTransactions.add(prepareTransaction(TransactionDuration.CONSTANT, TransactionType.INCOME));
        }
        for (int i = 0; i < constantExpanseCnt; i++) {
            userTransactions.add(prepareTransaction(TransactionDuration.CONSTANT, TransactionType.EXPANSE));
        }

        Map<String, ActualTransactionStats> map = actualTransactionStatsFactory.combineTransactionsFromList(userTransactions);

        //Single transactions
        ActualTransactionStats stats = map.get(TransactionDuration.SINGLE.toString());
        Assert.assertNotNull(stats);
        Assert.assertEquals(expanseCnt, stats.getExpensesCnt());
        Assert.assertEquals(incomeCnt, stats.getIncomeCnt());
        Assert.assertEquals(AMOUNT.multiply(new BigDecimal(expanseCnt)), stats.getExpensesSum());
        Assert.assertEquals(AMOUNT.multiply(new BigDecimal(incomeCnt)), stats.getIncomeSum());
        Assert.assertEquals(singleBalane, stats.getBalance());

        //Constant transactions
        stats = map.get(TransactionDuration.CONSTANT.toString());
        Assert.assertNotNull(stats);
        Assert.assertEquals(constantExpanseCnt, stats.getExpensesCnt());
        Assert.assertEquals(constantIncomeCnt, stats.getIncomeCnt());
        Assert.assertEquals(AMOUNT.multiply(new BigDecimal(constantExpanseCnt)), stats.getExpensesSum());
        Assert.assertEquals(AMOUNT.multiply(new BigDecimal(constantIncomeCnt)), stats.getIncomeSum());
        Assert.assertEquals(constantBalance, stats.getBalance());
    }

    @Test
    public void combineTransactionsFromList_PassEmptyList_ReturnMapWithValidIncomesAndExpansesByType() {
        List<UserTransactions> userTransactions = Collections.emptyList();

        Map<String, ActualTransactionStats> map = actualTransactionStatsFactory.combineTransactionsFromList(userTransactions);

        //Single transactions
        ActualTransactionStats stats = map.get(TransactionDuration.SINGLE.toString());
        Assert.assertNotNull(stats);
        Assert.assertEquals(0, stats.getExpensesCnt());
        Assert.assertEquals(0, stats.getIncomeCnt());
        Assert.assertEquals(BigDecimal.ZERO, stats.getExpensesSum());
        Assert.assertEquals(BigDecimal.ZERO, stats.getIncomeSum());
        Assert.assertEquals(BigDecimal.ZERO, stats.getBalance());

        //Constant transactions
        stats = map.get(TransactionDuration.CONSTANT.toString());
        Assert.assertNotNull(stats);
        Assert.assertEquals(0, stats.getExpensesCnt());
        Assert.assertEquals(0, stats.getIncomeCnt());
        Assert.assertEquals(BigDecimal.ZERO, stats.getExpensesSum());
        Assert.assertEquals(BigDecimal.ZERO, stats.getIncomeSum());
        Assert.assertEquals(BigDecimal.ZERO, stats.getBalance());
    }

    @Test
    public void combineTransactionsFromList_PassNullList_ReturnMapWithValidIncomesAndExpansesByType() {
        Map<String, ActualTransactionStats> map = actualTransactionStatsFactory.combineTransactionsFromList(null);

        //Single transactions
        ActualTransactionStats stats = map.get(TransactionDuration.SINGLE.toString());
        Assert.assertNotNull(stats);
        Assert.assertEquals(0, stats.getExpensesCnt());
        Assert.assertEquals(0, stats.getIncomeCnt());
        Assert.assertEquals(BigDecimal.ZERO, stats.getExpensesSum());
        Assert.assertEquals(BigDecimal.ZERO, stats.getIncomeSum());
        Assert.assertEquals(BigDecimal.ZERO, stats.getBalance());

        //Constant transactions
        stats = map.get(TransactionDuration.CONSTANT.toString());
        Assert.assertNotNull(stats);
        Assert.assertEquals(0, stats.getExpensesCnt());
        Assert.assertEquals(0, stats.getIncomeCnt());
        Assert.assertEquals(BigDecimal.ZERO, stats.getExpensesSum());
        Assert.assertEquals(BigDecimal.ZERO, stats.getIncomeSum());
        Assert.assertEquals(BigDecimal.ZERO, stats.getBalance());
    }

    private UserTransactions prepareTransaction(TransactionDuration transactionDuration, TransactionType transactionType) {
        UserTransactions userTransactions = new UserTransactions();
        userTransactions.setAmount(AMOUNT);
        userTransactions.setAppUser(APP_USER);
        userTransactions.setId(ID);
        userTransactions.setTransactionDuration(transactionDuration);
        userTransactions.setTransactionGroup(TRANSACTION_GROUP);
        userTransactions.setTransactionType(transactionType);
        return userTransactions;
    }

    private Cycle prepareCycle() {
        Cycle cycle = new Cycle();
        cycle.setSaveGoal(AMOUNT);
        return cycle;
    }
}