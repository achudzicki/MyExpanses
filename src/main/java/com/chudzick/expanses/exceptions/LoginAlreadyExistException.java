package com.chudzick.expanses.exceptions;

public class LoginAlreadyExistException extends Exception {

    public LoginAlreadyExistException(String message) {
        super(message);
    }

    public LoginAlreadyExistException() {
        super();
    }
}
