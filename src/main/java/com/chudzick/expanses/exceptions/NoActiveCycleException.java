package com.chudzick.expanses.exceptions;

import com.chudzick.expanses.domain.ApplicationActions;

public class NoActiveCycleException extends CommonActionExceptions {
    private static final String DEFAULT_MESSAGE = "Użytkownik nie posiada aktywnego cyklu";

    public NoActiveCycleException(ApplicationActions applicationActions) {
        super(applicationActions, DEFAULT_MESSAGE);
    }
}
