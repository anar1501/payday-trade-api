package com.paydaytrade.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Email is incorrect!")
public class EmailIsIncorrectException extends RuntimeException{
    public EmailIsIncorrectException() {
        super("6Email is incorrect!");
    }

}
