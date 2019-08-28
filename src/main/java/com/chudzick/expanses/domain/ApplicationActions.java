package com.chudzick.expanses.domain;

public enum ApplicationActions {
    DELETE_TRANSACTION("Usunięcie transakcji"),
    DELETE_GROUP("Usunięcie grupy tansakcji"),
    ADD_TRANSACTION("Dodanie nowej transackcji"),
    ADD_CONSTANT_TRANSACTION("Dodanie nowych wydatków stałych");

    private String actionName;

    ApplicationActions(String actionName) {
        this.actionName = actionName;
    }

    public String getActionName() {
        return actionName;
    }
}
