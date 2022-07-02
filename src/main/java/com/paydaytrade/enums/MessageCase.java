package com.paydaytrade.enums;

public enum MessageCase {
    EMPLOYEE_NOT_FOUND("Such Employee not found"),
    EMPLOYEE_CREATED("Employee successfully created"),
    USER_NOT_FOUND("Such user not found"),
    USERNAME_ALREADY_TAKEN("Such username already registered"),
    USER_UNCONFIRMED("User unconfirmed!!"),
    USER_ALREADY_CONFIRMED("Your Confirmation code already confirmed!!"),
    REGISTRATION_SUCCESSFULLY_CONFIRMED("Your Registration successfully Confirmed"),
    RESEND_EMAIL_SUCCESSFULLY_SENT("Your Confirmation link successfully sended your email"),
    PASSWORD_CONFIRMATION_LINK("Your forget password code successfully sent your email"),
    EMAIL_IS_INCORRECT("Email is incorrect!"),
    PASSWORD_SUCCESFULLY_CHANGED("Your new password successfully changed"),
    SIX_DIGIT_CODE("6 digit code is wrong"),
    EXPIRATION_TIME_IS_EXPIRED("Your Confirmed link of Expiration Time is Expired!!"),
    EAMIL_ALREADY_TAKEN("Such eamil already registered"),
    SUCCESSFULLY_REGISTERED("User successfully registered"),
    WRONG_PASSWORD("Password incorrect"),
    EMPLOYEE_UPDATED("Employee successfully updated"),
    EMPLOYEE_DELETED("Employee successfully deleted"),
    EMPLOYEE_DOES_NOT_EXIST("Employee does not exist"),
    SUCCESSFULLY_LOGINED("User successfully logined");


    private String message;

    MessageCase(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}