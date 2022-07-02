package com.paydaytrade.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "User unconfirmed!!")
public class UnconfirmedException extends RuntimeException{
    public UnconfirmedException() {
        super("User unconfirmed!!");
    }

}
