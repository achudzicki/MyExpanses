package com.chudzick.expanses.exceptions;

import com.chudzick.expanses.domain.ApplicationActions;

public class ImportTransactionException extends CommonActionExceptions {
    public ImportTransactionException(ApplicationActions action, String message) {
        super(action, message);
    }
}
