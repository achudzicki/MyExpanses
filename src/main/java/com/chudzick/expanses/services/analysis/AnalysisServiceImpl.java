package com.chudzick.expanses.services.analysis;

import com.chudzick.expanses.domain.analysis.AverageExpanse;
import com.chudzick.expanses.domain.expanses.ConstantTransaction;
import com.chudzick.expanses.domain.expanses.SingleTransaction;
import com.chudzick.expanses.domain.expanses.UserTransactions;
import com.chudzick.expanses.domain.expanses.dto.ConstantTransactionDto;
import com.chudzick.expanses.domain.expanses.dto.SingleTransactionDto;
import com.chudzick.expanses.services.transactions.ConstantTransactionService;
import com.chudzick.expanses.services.transactions.CycleService;
import com.chudzick.expanses.services.transactions.SingleTransactionService;
import com.chudzick.expanses.util.ListsUnion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AnalysisServiceImpl implements AnalysisService {

    @Autowired
    private CycleService cycleService;

    @Autowired
    private SingleTransactionService<SingleTransaction, SingleTransactionDto> singleTransactionService;

    @Autowired
    private ConstantTransactionService<ConstantTransaction, ConstantTransactionDto> constantTransactionService;

    @Override
    @Transactional
    public List<AverageExpanse> getAverageExpansesList() {
        List<SingleTransaction> allUserTransactions = singleTransactionService.findAllUserTransactions();
        List<ConstantTransaction> allUserConstantTransaction = constantTransactionService.findAllUserTransactions();
        List<UserTransactions> allTransactions = ListsUnion.union(allUserConstantTransaction, allUserTransactions);
        long userCyclesCnt = cycleService.countUserCycles();

        Map<Long, AverageExpanse> averageExpansesMap = new HashMap<>();
        for (UserTransactions transaction : allTransactions) {
            AverageExpanse temp = averageExpansesMap.get(transaction.getTransactionGroup().getId());
            if (temp == null) {
                averageExpansesMap.put(transaction.getTransactionGroup().getId(), new AverageExpanse(transaction, userCyclesCnt));
            } else {
                temp.update(transaction);
            }
        }

        return prepareSortedList(averageExpansesMap.values());
    }


    private LinkedList<AverageExpanse> prepareSortedList(Collection<AverageExpanse> averageExpanses) {
        return averageExpanses.stream()
                .sorted(Comparator.comparing(AverageExpanse::getIncomeAverage)
                        .thenComparing(AverageExpanse::getExpanseAverage))
                .collect(Collectors.toCollection(LinkedList::new));
    }
}
