package com.paydaytrade.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Such user not found")
public class UsernameNotFoundException extends RuntimeException{
    public UsernameNotFoundException() {
        super("Such user not found");
    }


}