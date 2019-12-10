package com.chudzick.expanses.domain;

public enum ApplicationActions {
    DELETE_TRANSACTION("Usunięcie transakcji"),
    DELETE_CONSTANT_TRANSACTION("Usunięcie transakcji stałej"),
    DELETE_GROUP("Usunięcie grupy tansakcji"),
    ADD_TRANSACTION("Dodanie nowej transackcji"),
    ADD_CONSTANT_TRANSACTION("Dodanie nowych wydatków stałych"),
    UPDATE_CYCLE_SETTINGS("Uaktualnienie ustawień cyklu"),
    IMPORT_TRANSACTIONS("Importowanie transakcji z pliku"),
    MANAGE_ARCHIVE_CYCLES("Zarządzanie cyklami"),
    SHOW_ANALYSIS("Statystyki cyklu"),
    ACCEPT_INVITATION("Akceptacja zaproszenia");

    private String actionName;

    ApplicationActions(String actionName) {
        this.actionName = actionName;
    }

    public String getActionName() {
        return actionName;
    }
}
