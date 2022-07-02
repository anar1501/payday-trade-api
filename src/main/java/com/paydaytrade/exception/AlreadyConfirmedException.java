package com.paydaytrade.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Your Confirmation code already confirmed!!")
public class AlreadyConfirmedException extends RuntimeException{
    public AlreadyConfirmedException() {
        super("Your Confirmation code already confirmed!!");
    }

}
