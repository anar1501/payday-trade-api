package com.paydaytrade.data.dto.response;

import lombok.Data;

import java.io.Serializable;

@Data
public class LoginResponseDto implements Serializable {
    private String token;
    private String tokeType="Bearer";

    public LoginResponseDto(String token) {
        this.token = token;
    }
}
