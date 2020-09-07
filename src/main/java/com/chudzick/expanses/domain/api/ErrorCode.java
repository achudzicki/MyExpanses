package com.chudzick.expanses.domain.api;

public enum ErrorCode {

    AUTH_BAD_CREDENTIALS("A_001", "Bad credentials passed"),
    AUTH_EMPTY_GRANT_TYPE("A_002", "Null or empty grant type passed"),
    AUTH_WRONG_GRANT_TYPE("A_003", "Wrong grant type passed"),
    AUTH_WRONG_USERNAME_PASSWORD("A_004", "Wrong username or password passed"),

    REGISTER_LOGIN_ALREADY_EXIST("R001", "Login already exist");

    private String errorCode;
    private String errMsq;

    ErrorCode(String errorCode, String errMsq) {
        this.errorCode = errorCode;
        this.errMsq = errMsq;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrMsq() {
        return errMsq;
    }
}
