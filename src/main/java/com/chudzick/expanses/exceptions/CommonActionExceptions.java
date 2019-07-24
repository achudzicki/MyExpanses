package com.chudzick.expanses.exceptions;

import com.chudzick.expanses.domain.ApplicationActions;

public class CommonActionExceptions extends Throwable {
    private final ApplicationActions action;

    public CommonActionExceptions(ApplicationActions action, String message) {
        super(message);
        this.action = action;
    }

    public ApplicationActions getAction() {
        return action;
    }
}
