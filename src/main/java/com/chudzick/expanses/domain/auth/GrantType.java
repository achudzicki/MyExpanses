package com.chudzick.expanses.domain.auth;

public enum GrantType {
    USERNAME_PASSWORD("username_password"),
    REFRESH_TOKEN("refresh_token");

    private String type;

    GrantType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
