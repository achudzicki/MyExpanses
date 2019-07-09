package com.chudzick.expanses.domain.expanses;

public enum TransactionType {
    INCOME("wydatek"),
    EXPANSE("przychód");

    private String name;

    TransactionType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
