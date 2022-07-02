package com.paydaytrade.service;

import com.paydaytrade.data.dto.request.LoginRequestDto;
import com.paydaytrade.data.dto.request.RegisterRequestDto;
import com.paydaytrade.data.dto.request.ResetPasswordRequestDto;
import com.paydaytrade.data.dto.response.LoginResponseDto;
import org.springframework.http.ResponseEntity;

public interface UserService {

    LoginResponseDto login(LoginRequestDto loginRequestDto);

    ResponseEntity<String> register(RegisterRequestDto registerRequestDto);

    void registerConfirm(String activationCode);

    void resendEmail(String email);

    void forgetPassword(String email);

    void resetPassword(ResetPasswordRequestDto requestDto);
}
