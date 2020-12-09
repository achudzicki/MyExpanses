package com.chudzick.expanses.controllers.rest;

import com.chudzick.expanses.beans.rest.RestRequestBean;
import com.chudzick.expanses.domain.application.ApplicationAccess;
import com.chudzick.expanses.domain.expanses.ConstantTransaction;
import com.chudzick.expanses.domain.expanses.SingleTransaction;
import com.chudzick.expanses.domain.expanses.UserTransactions;
import com.chudzick.expanses.domain.expanses.dto.ConstantTransactionDto;
import com.chudzick.expanses.domain.expanses.dto.SingleTransactionDto;
import com.chudzick.expanses.domain.statictics.ActualTransactionStats;
import com.chudzick.expanses.domain.users.AppUser;
import com.chudzick.expanses.factories.ActualTransactionStatsFactory;
import com.chudzick.expanses.services.application.ApplicationAccessService;
import com.chudzick.expanses.services.rest.MagicMirrorService;
import com.chudzick.expanses.services.transactions.ConstantTransactionService;
import com.chudzick.expanses.services.transactions.SingleTransactionService;
import com.chudzick.expanses.util.ListsUnion;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "magic-mirror")
public class MagicMirrorController {
    @Autowired
    private SingleTransactionService<SingleTransaction, SingleTransactionDto> singleTransactionService;

    @Autowired
    private ConstantTransactionService<ConstantTransaction, ConstantTransactionDto> constantTransactionService;

    @Autowired
    private MagicMirrorService magicMirrorService;

    @Autowired
    private ApplicationAccessService applicationAccessService;

    @Autowired
    private RestRequestBean restRequestBean;

    @GetMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<String> getMagicMirrorData(@RequestParam("applicationId") String applicationId) {
        if (applicationId == null || applicationId.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        Optional<ApplicationAccess> applicationAccess = applicationAccessService.findByApplicationId(applicationId);

        if (!applicationAccess.isPresent()) {
            return ResponseEntity.noContent().build();
        }
        AppUser appUser = applicationAccess.get().getAppUser();

        restRequestBean.setRequestUser(appUser);
        restRequestBean.setRestRequest(true);

        List<SingleTransaction> allSingle = singleTransactionService.findAll()
                .stream()
                .sorted(Comparator.comparing(SingleTransaction::getTransactionDate))
                .collect(Collectors.toCollection(LinkedList::new));
        List<ConstantTransaction> allConstant = constantTransactionService.findAllActiveConstantTransactions();
        List<UserTransactions> allTransactions = ListsUnion.union(allConstant, allSingle);
        Map<String, ActualTransactionStats> actualTransactionStats = new ActualTransactionStatsFactory().combineTransactionsFromList(allTransactions);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("lastTransactions", magicMirrorService.prepareSingleTransactionArray(allSingle));
        jsonObject.put("userInformation", magicMirrorService.prepareUserInformation(appUser));
        jsonObject.put("balanceInformation", magicMirrorService.prepareBalanceInformation(actualTransactionStats));
        return ResponseEntity.ok(jsonObject.toString());
    }
}
