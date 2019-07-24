package com.chudzick.expanses.exceptions;

import com.chudzick.expanses.domain.ApplicationActions;

public class UserNotPermittedToActionException extends CommonActionExceptions {
    private static final String MESSAGE = "Próba nieuprawnionego dostępu do danego zasobu!";

    public UserNotPermittedToActionException(ApplicationActions action) {
        super(action, MESSAGE);
    }

}
