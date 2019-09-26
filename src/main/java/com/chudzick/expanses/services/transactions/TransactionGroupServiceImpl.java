package com.chudzick.expanses.services.transactions;

import com.chudzick.expanses.domain.expanses.TransactionGroup;
import com.chudzick.expanses.domain.expanses.dto.TransactionGroupDto;
import com.chudzick.expanses.domain.users.AppUser;
import com.chudzick.expanses.factories.TransactionGroupStaticFactory;
import com.chudzick.expanses.repositories.TransactionGroupRepository;
import com.chudzick.expanses.services.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionGroupServiceImpl implements TransactionGroupService {

    @Autowired
    private TransactionGroupRepository transactionGroupRepository;

    @Autowired
    private UserService userService;

    @Override
    @Transactional
    public List<TransactionGroup> getAllGroups() {
        AppUser appUser = userService.getCurrentLogInUser();

        return transactionGroupRepository.findAllByAppUser(appUser);
    }

    @Override
    @Transactional
    public TransactionGroup addNewTransactionGroup(TransactionGroupDto transactionGroupDto) {
        AppUser appUser = userService.getCurrentLogInUser();
        TransactionGroup newGroup = TransactionGroupStaticFactory.createFromDto(transactionGroupDto, appUser);
        return transactionGroupRepository.save(newGroup);
    }

    @Override
    @Transactional
    public void deleteTransactionGroup(long groupId) {
        transactionGroupRepository.deleteById(groupId);
    }

    @Override
    public TransactionGroup findById(long groupId) {
        Optional<TransactionGroup> foundTransactionGroup = transactionGroupRepository.findById(groupId);

        if (!foundTransactionGroup.isPresent()) {
            //TODO DOROBI EXCEPTION
            throw new RuntimeException();
        }
        return foundTransactionGroup.get();
    }
}
