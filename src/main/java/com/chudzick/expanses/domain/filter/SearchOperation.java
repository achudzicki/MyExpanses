package com.chudzick.expanses.domain.filter;

public enum SearchOperation {
    EQUALITY(":"), NEGATION("!"), GRATER_THAN(">"), LESS_THAN("<"), LIKE("%");

    private String operationChar;

    SearchOperation(String operationChar) {
        this.operationChar = operationChar;
    }

    public static SearchOperation getSearchOperation(final String input) {
        switch (input) {
            case ":" : return EQUALITY;
            case "!" : return NEGATION;
            case ">" : return GRATER_THAN;
            case "<" : return LESS_THAN;
            case "%" : return LIKE;
            default: throw new IllegalStateException();
        }
    }
}
