package com.chudzick.expanses.exceptions;

public class LoggedInUserNotFoundException extends RuntimeException {
    private static final String MESSAGE = "Can't found currently log in user";

    public LoggedInUserNotFoundException() {
        super(MESSAGE);
    }

    public LoggedInUserNotFoundException(String message) {
        super(message);
    }
}
