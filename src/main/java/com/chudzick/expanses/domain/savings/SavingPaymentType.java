package com.chudzick.expanses.domain.savings;

public enum SavingPaymentType {
    ADD("WPŁATA"),
    TAKE("WYPŁATA");

    private String name;

    SavingPaymentType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
