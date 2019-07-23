package com.chudzick.expanses.domain;

public enum  ApplicationActions {
    DELETE_TRANSACTION("Usunięcie transakcji"),
    DELETE_GROUP("Usunięcie grupy tansakcji");

    private String actionName;

    ApplicationActions(String actionName) {
        this.actionName = actionName;
    }

    public String getActionName() {
        return actionName;
    }
}
