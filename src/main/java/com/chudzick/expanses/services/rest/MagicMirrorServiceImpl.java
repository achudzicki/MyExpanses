package com.chudzick.expanses.services.rest;

import com.chudzick.expanses.domain.expanses.SingleTransaction;
import com.chudzick.expanses.domain.expanses.TransactionDuration;
import com.chudzick.expanses.domain.statictics.ActualTransactionStats;
import com.chudzick.expanses.domain.users.AppUser;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

@Service
public class MagicMirrorServiceImpl implements MagicMirrorService {
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("0.00");
    private static final int TRANSACTION_CNT = 8;

    @Override
    public JSONArray prepareSingleTransactionArray(List<SingleTransaction> singleTransactions) {
        JSONArray jsonArray = new JSONArray();
        int iterations = singleTransactions.size() > TRANSACTION_CNT ? TRANSACTION_CNT : singleTransactions.size();
        for (int i = 0; i < iterations; i++) {
            SingleTransaction singleTransaction = singleTransactions.get(i);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("type", singleTransaction.getTransactionType());
            jsonObject.put("date", singleTransaction.getTransactionDate());
            jsonObject.put("amount", DECIMAL_FORMAT.format(singleTransaction.getAmount()));
            jsonObject.put("groupName", singleTransaction.getTransactionGroup().getGorupName());
            jsonArray.put(jsonObject);
        }

        return jsonArray;
    }

    @Override
    public JSONObject prepareUserInformation(AppUser appUser) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", appUser.getName() + " " + appUser.getLastName());
        jsonObject.put("photoId", appUser.getId());
        return jsonObject;
    }

    @Override
    public JSONObject prepareBalanceInformation(Map<String, ActualTransactionStats> actualTransactionStats) {
        BigDecimal outcomes = actualTransactionStats.get(TransactionDuration.SINGLE.toString()).getExpensesSum().add(actualTransactionStats.get(TransactionDuration.CONSTANT.toString()).getExpensesSum());
        BigDecimal incomes = actualTransactionStats.get(TransactionDuration.SINGLE.toString()).getIncomeSum().add(actualTransactionStats.get(TransactionDuration.CONSTANT.toString()).getIncomeSum());
        BigDecimal balance = actualTransactionStats.get(TransactionDuration.SINGLE.toString()).getBalance().add(actualTransactionStats.get(TransactionDuration.CONSTANT.toString()).getBalance());

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("outcomes", DECIMAL_FORMAT.format(outcomes));
        jsonObject.put("incomes", DECIMAL_FORMAT.format(incomes));
        jsonObject.put("balance", DECIMAL_FORMAT.format(balance));
        return jsonObject;
    }
}
