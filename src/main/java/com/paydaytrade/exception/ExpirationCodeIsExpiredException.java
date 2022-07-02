package com.paydaytrade.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Your Confirmed link of Expiration Time is Expired!!")
public class ExpirationCodeIsExpiredException extends RuntimeException{
    public ExpirationCodeIsExpiredException() {
        super("Such eamil already registered !");
    }
}
