package com.chudzick.expanses.exceptions;

public class CycleImpositionException extends Exception {
    private static final String DEFAULT_MESSAGE = "Zmiana ustawień spowoduje nałożenie się dwóch cyklów";

    public CycleImpositionException() {
        super(DEFAULT_MESSAGE);
    }

    public CycleImpositionException(String message) {
        super(message);
    }
}
