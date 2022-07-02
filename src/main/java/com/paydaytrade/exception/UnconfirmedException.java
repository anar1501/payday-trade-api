package com.paydaytrade.exception;

public class UnconfirmedException extends RuntimeException{
    private String message;

    public UnconfirmedException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
