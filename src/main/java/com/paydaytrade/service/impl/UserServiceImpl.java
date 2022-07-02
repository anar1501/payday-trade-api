package com.paydaytrade.service.impl;

import com.paydaytrade.config.ModelMapperConfiguration;
import com.paydaytrade.data.dto.request.LoginRequestDto;
import com.paydaytrade.data.dto.request.RegisterRequestDto;
import com.paydaytrade.data.dto.request.ResetPasswordRequestDto;
import com.paydaytrade.data.dto.response.LoginResponseDto;
import com.paydaytrade.data.entity.User;
import com.paydaytrade.data.repository.UserRepository;
import com.paydaytrade.data.repository.UserStatusRepositry;
import com.paydaytrade.enums.MessageCase;
import com.paydaytrade.enums.UserStatusEnum;
import com.paydaytrade.exception.*;
import com.paydaytrade.security.jwt.JwtUtil;
import com.paydaytrade.service.UserService;
import com.paydaytrade.utils.GeneralUtils;
import com.paydaytrade.utils.MessageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.UUID;

import static com.paydaytrade.enums.MessageCase.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final static Date currentDate = new Date();

    @Value("${my.message.subject}")
    private String messageSubject;

    @Value("${my.message.body}")
    private String messageBody;

    private final UserRepository userRepository;
    private final UserStatusRepositry userStatusRepositry;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final MessageUtils messageUtils;
    private final JwtUtil jwtUtil;

    @Transactional
    @Override
    public LoginResponseDto login(LoginRequestDto loginRequestDto) {

        User user = userRepository.findByEmailOrUsername(loginRequestDto.getUsernameOrEmail(), loginRequestDto.getUsernameOrEmail())
                .orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND.getMessage()));
        boolean isTrue = passwordEncoder.matches(loginRequestDto.getPassword(), user.getPassword());
        if (!isTrue) {
            throw new WrongPasswordException(WRONG_PASSWORD.getMessage());
        }
        if (user.getStatus().getId().equals(UserStatusEnum.UNCONFIRMED.getStatusId())) {
            throw new UnconfirmedException(USER_UNCONFIRMED.getMessage());
        }

        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequestDto.getUsernameOrEmail(), loginRequestDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        return new LoginResponseDto(jwtUtil.generateToke(authenticate));

    }

    @Override
    public ResponseEntity<String> register(RegisterRequestDto registerRequestDto) {
        if (userRepository.existsByUsername(registerRequestDto.getUsername())) {
            return new ResponseEntity<>(MessageCase.USERNAME_ALREADY_TAKEN.getMessage(), HttpStatus.BAD_REQUEST);
        } else if (userRepository.existsByEmail(registerRequestDto.getEmail())) {
            return new ResponseEntity<>(MessageCase.EAMIL_ALREADY_TAKEN.getMessage(), HttpStatus.BAD_REQUEST);
        }
        User user = ModelMapperConfiguration.map(registerRequestDto, User.class);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setExpiredDate(GeneralUtils.prepareRegistrationExpirationDate());
        user.setActivationCode(passwordEncoder.encode(UUID.randomUUID().toString()));
        User saveUser = userRepository.save(user);
        String confirmLink = "http://localhost:8080/api/v1/auth/register-confirm?activationcode=" + saveUser.getActivationCode();
        messageUtils.sendAsync(saveUser.getEmail(), messageSubject, messageBody + confirmLink);
        return new ResponseEntity<>(MessageCase.SUCCESSFULLY_REGISTERED.getMessage(), HttpStatus.CREATED);
    }

    @Transactional
    @Override
    public void registerConfirm(String activationCode) {
        User user = userRepository.findByActivationCode(activationCode);
        if (user.getStatus().getId().equals(UserStatusEnum.CONFIRMED.getStatusId())) {
            throw new AlreadyConfirmedException(MessageCase.USER_ALREADY_CONFIRMED.getMessage());
        }
        Date expiredDate = user.getExpiredDate();
        if (expiredDate.before(currentDate)) {
            throw new ExpirationCodeIsExpiredException(MessageCase.EXPIRATION_TIME_IS_EXPIRED.getMessage());
        } else {
            user.setStatus(userStatusRepositry.findUserStatusById(UserStatusEnum.CONFIRMED.getStatusId()));
            userRepository.save(user);
        }

    }

    @Transactional
    @Override
    public void resendEmail(String email) {
        User user = userRepository.getByEmail(email);
        String activationCode = UUID.randomUUID().toString();
        String encode = passwordEncoder.encode(activationCode);
        user.setActivationCode(encode);
        user.setExpiredDate(GeneralUtils.prepareRegistrationExpirationDate());
        User saveUser = userRepository.save(user);
        String confirmLink = "http://localhost:8080/api/v1/auth/register-confirm?activationcode=" + saveUser.getActivationCode();
        messageUtils.sendAsync(saveUser.getEmail(), messageSubject, messageBody + confirmLink);
    }

    @Override
    public void forgetPassword(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new EmailIsIncorrectException(EMAIL_IS_INCORRECT.getMessage()));
        user.setSixDigitCode(GeneralUtils.getRandomNumberString());
        user.setForgetPasswordExpiredDate(GeneralUtils.prepareForgetPasswordExpirationDate());
        User saveUser = userRepository.save(user);
        messageUtils.sendAsync(saveUser.getEmail(), messageSubject, messageBody + saveUser.getSixDigitCode());
    }

    @Override
    public void resetPassword(ResetPasswordRequestDto requestDto) {
        User user = userRepository.findBySixDigitCode(requestDto.getSixDigitCode()).orElseThrow(() -> new SixDigitIncorrectException(SIX_DIGIT_CODE.getMessage()));
        Date expiredDate = user.getForgetPasswordExpiredDate();
        if (expiredDate.before(currentDate)){
            throw new ExpirationCodeIsExpiredException(EXPIRATION_TIME_IS_EXPIRED.getMessage());
        }
        user.setPassword(passwordEncoder.encode(requestDto.getNewPassword()));
        userRepository.save(user);
    }
}
