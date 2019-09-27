package com.chudzick.expanses.services.permissions;

import com.chudzick.expanses.domain.expanses.UserTransactions;
import com.chudzick.expanses.exceptions.UserNotPermittedToActionException;

public interface PermissionsService {

    boolean checkUserPermissionsToDeleteGroup(long groupId) throws UserNotPermittedToActionException;

    boolean checkPermissionToDeleteTransaction(UserTransactions transaction) throws UserNotPermittedToActionException;
}
