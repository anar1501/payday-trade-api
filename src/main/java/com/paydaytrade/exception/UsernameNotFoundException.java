package com.paydaytrade.exception;

public class UsernameNotFoundException extends RuntimeException{
    private String message;

    public UsernameNotFoundException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

}