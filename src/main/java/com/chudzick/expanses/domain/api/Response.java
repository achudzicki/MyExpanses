package com.chudzick.expanses.domain.api;

import java.util.List;

public class Response<K> {
    private List<ResponseError> responseErrors;
    private String message;
    private K content;

    public Response() {
    }

    public Response(String message) {
        this.message = message;
    }

    public Response(List<ResponseError> responseErrors) {
        this.responseErrors = responseErrors;
    }

    public Response(K content) {
        this.content = content;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<ResponseError> getValidationErrors() {
        return responseErrors;
    }

    public void setValidationErrors(List<ResponseError> responseErrors) {
        this.responseErrors = responseErrors;
    }

    public K getContent() {
        return content;
    }

    public void setContent(K content) {
        this.content = content;
    }
}
