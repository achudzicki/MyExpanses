package com.chudzick.expanses.exceptions;

import com.chudzick.expanses.domain.ApplicationActions;

public class AppObjectNotFoundException extends CommonActionExceptions {
    private static final String DEFAULT_MESSAGE = "Nie znaleziono szukanych danych";

    public AppObjectNotFoundException(ApplicationActions action, String message) {
        super(action, message);
    }

    public AppObjectNotFoundException(ApplicationActions applicationActions) {
        super(applicationActions,DEFAULT_MESSAGE);
    }
}
