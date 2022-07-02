package com.paydaytrade.enums;

public enum MessageCase {
    USER_NOT_FOUND("Such user not found");

    private final String message;

    MessageCase(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}