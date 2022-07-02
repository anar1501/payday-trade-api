package com.paydaytrade.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Such eamil already registered !")
public class EmailAlreadyTaken extends RuntimeException{
    public EmailAlreadyTaken() {
        super("Such eamil already registered !");
    }
}
