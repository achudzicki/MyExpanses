package com.chudzick.expanses.services.transactions;

import com.chudzick.expanses.domain.expanses.TransactionGroup;
import com.chudzick.expanses.domain.expanses.TransactionGroupDto;
import com.chudzick.expanses.domain.users.AppUser;
import com.chudzick.expanses.factories.TransactionGroupStaticFactory;
import com.chudzick.expanses.repositories.TransactionGroupRepository;
import com.chudzick.expanses.services.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionGroupServiceImpl implements TransactionGroupService {

    @Autowired
    private TransactionGroupRepository transactionGroupRepository;

    @Autowired
    private UserService userService;

    @Override
    public List<TransactionGroup> getAllGroups() {
        return transactionGroupRepository.findAll();
    }

    @Override
    public TransactionGroup addNewTransactionGroup(TransactionGroupDto transactionGroupDto) {
        AppUser appUser = userService.getCurrentLogInUser();
        TransactionGroup newGroup = TransactionGroupStaticFactory.createFromDto(transactionGroupDto, appUser);
        return transactionGroupRepository.save(newGroup);
    }
}
