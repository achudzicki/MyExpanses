package com.chudzick.expanses.exceptions;

import com.chudzick.expanses.domain.ApplicationActions;

public class InvitationNotFoundException extends CommonActionExceptions {
    private final static String MESSAGE = "Nie znaleziono zaproszenia";

    public InvitationNotFoundException(ApplicationActions action) {
        super(action, MESSAGE);
    }

    public InvitationNotFoundException(ApplicationActions action, String message) {
        super(action, message);
    }
}
