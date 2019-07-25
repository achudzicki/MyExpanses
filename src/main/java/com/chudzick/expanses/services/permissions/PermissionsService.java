package com.chudzick.expanses.services.permissions;

import com.chudzick.expanses.domain.expanses.SingleTransaction;
import com.chudzick.expanses.exceptions.UserNotPermittedToActionException;

public interface PermissionsService {

    boolean checkUserPermissionsToDeleteGroup(long groupId) throws UserNotPermittedToActionException;

    boolean checkPermissionToDeleteSingleTransaction(SingleTransaction singleTransaction) throws UserNotPermittedToActionException;
}
