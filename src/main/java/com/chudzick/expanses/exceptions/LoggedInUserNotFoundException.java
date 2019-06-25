package com.chudzick.expanses.exceptions;

public class LoggedInUserNotFoundException extends RuntimeException {

    public LoggedInUserNotFoundException() {
    }

    public LoggedInUserNotFoundException(String message) {
        super(message);
    }
}
