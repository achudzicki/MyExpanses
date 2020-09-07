package com.chudzick.expanses.domain.api;

public class ResponseError {
    private String code;
    private String message;

    public ResponseError(ErrorCode errorCode) {
        this.code = errorCode.getErrorCode();
        this.message = errorCode.getErrMsq();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
