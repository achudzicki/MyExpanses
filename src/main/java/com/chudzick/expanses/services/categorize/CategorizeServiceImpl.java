package com.chudzick.expanses.services.categorize;

import com.chudzick.expanses.domain.categorize.TransactionGroupCategorizeData;
import com.chudzick.expanses.domain.expanses.AccountOperationDto;
import com.chudzick.expanses.domain.expanses.TransactionGroup;
import com.chudzick.expanses.domain.expanses.dto.SingleTransactionDto;
import com.chudzick.expanses.domain.users.AppUser;
import com.chudzick.expanses.repositories.CategorizeRepository;
import com.chudzick.expanses.services.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class CategorizeServiceImpl implements CategorizeService {

    private Set<String> commonWords = new HashSet<>();
    private static final int MIN_WORD_LEN = 3;

    @Autowired
    private CategorizeRepository categorizeRepository;

    @Autowired
    private UserService userService;

    @PostConstruct
    public void setUpCommonWords() {
        commonWords = Stream.of(CommonWords.values()).map(CommonWords::getWord).collect(Collectors.toCollection(HashSet::new));
    }

    @Override
    public void addCategorizeData(List<SingleTransactionDto> transactions) {
        AppUser currentUser = userService.getCurrentLogInUser();
        for (SingleTransactionDto transaction : transactions) {
            String[] words = transaction.getImportedTransactionTitle().split("\\s+");
            for (String word : words) {
                if (word.length() > MIN_WORD_LEN && !commonWords.contains(word.toLowerCase()) && isNumericalOnly(word)) {
                    Optional<TransactionGroupCategorizeData> data = categorizeRepository.findByTransactionGroupAndKey(transaction.getTransactionGroup(), word);

                    if (data.isPresent()) {
                        data.get().setStrength(data.get().getStrength() + 1);
                        categorizeRepository.save(data.get());
                    } else {
                        TransactionGroupCategorizeData newData = new TransactionGroupCategorizeData();
                        newData.setKey(word);
                        newData.setTransactionGroup(transaction.getTransactionGroup());
                        newData.setStrength(1);
                        newData.setAppUser(currentUser);
                        categorizeRepository.save(newData);
                    }
                }
            }
        }
    }

    @Override
    public Map<String, List<TransactionGroupCategorizeData>> getCategorizeData() {
        AppUser currentUser = userService.getCurrentLogInUser();
        List<TransactionGroupCategorizeData> userData = categorizeRepository.findAllByAppUser(currentUser);

        return userData.stream().collect(Collectors.
                groupingBy(TransactionGroupCategorizeData::getKey, Collectors.toCollection(ArrayList::new)));
    }

    @Override
    public void addTipToOperation(Map<String, List<TransactionGroupCategorizeData>> categorizeData, AccountOperationDto dto) {
        Map<TransactionGroup, Long> groupsStrength = new HashMap<>();
        String[] words = dto.getDescription().split("\\s+");
        for (String word : words) {
            if (word.length() > MIN_WORD_LEN && !commonWords.contains(word.toLowerCase()) && isNumericalOnly(word)) {
                List<TransactionGroupCategorizeData> data = categorizeData.get(word);
                if (data != null) {
                    data.forEach(d -> {
                        long actual = groupsStrength.getOrDefault(d.getTransactionGroup(), 0L);
                        groupsStrength.put(d.getTransactionGroup(), actual + d.getStrength());
                    });
                }
            }
        }

        groupsStrength.entrySet().stream()
                .max(Comparator.comparing(Map.Entry::getValue))
                .ifPresent(entry -> dto.setTipGroup(entry.getKey()));
    }

    private boolean isNumericalOnly(String word) {
        Pattern pattern = Pattern.compile("[a-zA-Z]+");
        Matcher matcher = pattern.matcher(word);
        return matcher.matches();
    }
}
