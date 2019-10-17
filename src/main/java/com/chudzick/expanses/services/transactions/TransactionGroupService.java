package com.chudzick.expanses.services.transactions;

import com.chudzick.expanses.domain.expanses.TransactionGroup;
import com.chudzick.expanses.domain.expanses.dto.TransactionGroupDto;

import java.util.List;

public interface TransactionGroupService {

    List<TransactionGroup> getAllGroups();

    TransactionGroup addNewTransactionGroup(TransactionGroupDto transactionGroupDto);

    void deleteTransactionGroup(long groupId);

    TransactionGroup findById(long groupId);
}
