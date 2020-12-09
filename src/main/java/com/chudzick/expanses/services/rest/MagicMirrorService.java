package com.chudzick.expanses.services.rest;

import com.chudzick.expanses.domain.expanses.SingleTransaction;
import com.chudzick.expanses.domain.statictics.ActualTransactionStats;
import com.chudzick.expanses.domain.users.AppUser;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

public interface MagicMirrorService {
    JSONArray prepareSingleTransactionArray(List<SingleTransaction> singleTransactions);

    JSONObject prepareUserInformation(AppUser appUser);

    JSONObject prepareBalanceInformation(Map<String, ActualTransactionStats> actualTransactionStats);
}
