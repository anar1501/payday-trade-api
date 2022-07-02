package com.paydaytrade.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Such username already registered !")
public class UsernameAlreadyTaken extends RuntimeException{
    public UsernameAlreadyTaken() {
        super("Such username already registered !");
    }
}
