package com.github.fabriciolfj.outbox.exception;

import java.util.ResourceBundle;

public enum ErrorEnum {

    BUSINESS_MESSAGE,
    VALIDATION_MESSAGE,
    DUPLICATE_TRANSACTION_MESSAGE;


    public String getMessage() {
        var bundle = ResourceBundle.getBundle("messages/exception");

        return bundle.getString(this.name() + ".message");
    }
}
