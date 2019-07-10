package com.chudzick.expanses.services.permissions;

import com.chudzick.expanses.domain.expanses.TransactionGroup;
import com.chudzick.expanses.domain.users.AppUser;
import com.chudzick.expanses.repositories.TransactionGroupRepository;
import com.chudzick.expanses.services.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PermissionsServiceImpl implements PermissionsService {

    @Autowired
    private UserService userService;

    @Autowired
    private TransactionGroupRepository transactionGroupRepository;

    @Override
    public boolean checkUserPermissionsToDeleteGroup(long groupId) {
        AppUser appUser = userService.getCurrentLogInUser();

        Optional<TransactionGroup> foundTransaction = transactionGroupRepository.findByIdAndAppUser(groupId, appUser);
        return foundTransaction.isPresent();
    }
}
