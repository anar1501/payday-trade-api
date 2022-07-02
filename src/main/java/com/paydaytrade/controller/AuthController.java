package com.paydaytrade.controller;

import com.paydaytrade.data.dto.request.LoginRequestDto;
import com.paydaytrade.data.dto.request.RegisterRequestDto;
import com.paydaytrade.data.dto.request.ResetPasswordRequestDto;
import com.paydaytrade.data.dto.response.LoginResponseDto;
import com.paydaytrade.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping(value = "/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequestDto) {
        return ResponseEntity.ok(userService.login(loginRequestDto));
    }


    @PostMapping(value = "/register")
    public HttpStatus register(@RequestBody RegisterRequestDto registerRequestDto) {
         userService.register(registerRequestDto);
         return HttpStatus.CREATED;
    }

    @GetMapping(value = "/register-confirm")
    public HttpStatus registerConfirm(@RequestParam(value = "activationcode") String activationCode) {
        userService.registerConfirm(activationCode);
        return HttpStatus.OK;
    }

    @GetMapping(value = "/resend")
    public HttpStatus resendEmail(@RequestParam(value = "email")String email){
        userService.resendEmail(email);
        return HttpStatus.OK;
    }

    @GetMapping(value = "/forget-password")
    public HttpStatus forgetPassword(@RequestParam(value = "email")String email){
        userService.forgetPassword(email);
        return HttpStatus.OK;
    }

    @PutMapping(value = "/reset-password")
    public HttpStatus resetPassword(@RequestBody ResetPasswordRequestDto requestDto){
        userService.resetPassword(requestDto);
        return HttpStatus.OK;
    }
}
