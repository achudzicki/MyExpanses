package com.chudzick.expanses.services.permissions;

import com.chudzick.expanses.domain.ApplicationActions;
import com.chudzick.expanses.domain.expanses.TransactionGroup;
import com.chudzick.expanses.domain.expanses.UserTransactions;
import com.chudzick.expanses.domain.users.AppUser;
import com.chudzick.expanses.exceptions.UserNotPermittedToActionException;
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
    public boolean checkUserPermissionsToDeleteGroup(long groupId) throws UserNotPermittedToActionException {
        AppUser appUser = userService.getCurrentLogInUser();

        Optional<TransactionGroup> foundTransaction = transactionGroupRepository.findByIdAndAppUser(groupId, appUser);
        if (!foundTransaction.isPresent()) {
            throw new UserNotPermittedToActionException(ApplicationActions.DELETE_GROUP);
        }
        return true;
    }

    @Override
    public boolean checkPermissionToDeleteTransaction(UserTransactions singleTransaction) throws UserNotPermittedToActionException {
        AppUser currentUser = userService.getCurrentLogInUser();

        if (!singleTransaction.getAppUser().getId().equals(currentUser.getId())) {
            throw new UserNotPermittedToActionException(ApplicationActions.DELETE_TRANSACTION);
        }
        return true;
    }
}
