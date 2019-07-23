package com.chudzick.expanses.exceptions;

import com.chudzick.expanses.domain.ApplicationActions;

public class UserNotPermittedToActionException extends Throwable {
    private final ApplicationActions action;
    private static final String MESSAGE = "Próba nieuprawnionego dostępu do danego zasobu!";

    public UserNotPermittedToActionException(ApplicationActions action) {
        super(MESSAGE);
        this.action = action;
    }

    public ApplicationActions getAction() {
        return action;
    }
}
