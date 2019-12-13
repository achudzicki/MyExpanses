package com.chudzick.expanses.services.categorize;

import com.chudzick.expanses.domain.categorize.TransactionGroupCategorizeData;
import com.chudzick.expanses.domain.expanses.AccountOperationDto;
import com.chudzick.expanses.domain.expanses.dto.SingleTransactionDto;

import java.util.List;
import java.util.Map;

public interface CategorizeService {

    void addCategorizeData(List<SingleTransactionDto> transactions);

    Map<String,List<TransactionGroupCategorizeData>> getCategorizeData();

    void addTipToOperation(Map<String, List<TransactionGroupCategorizeData>> categorizeData, AccountOperationDto dto);
}
