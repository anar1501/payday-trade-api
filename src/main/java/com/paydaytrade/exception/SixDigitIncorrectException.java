package com.paydaytrade.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "6 digit code is wrong")
public class SixDigitIncorrectException extends RuntimeException{
    public SixDigitIncorrectException() {
        super("6 digit code is wrong");
    }
}
