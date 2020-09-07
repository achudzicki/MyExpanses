package com.chudzick.expanses.exceptions;

public class LoginAlreadyExistException extends RuntimeException {

    public LoginAlreadyExistException(String message) {
        super(message);
    }

    public LoginAlreadyExistException() {
        super();
    }
}
