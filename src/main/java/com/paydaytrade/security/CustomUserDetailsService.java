package com.paydaytrade.security;

import com.paydaytrade.data.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import static com.paydaytrade.enums.MessageCase.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        return new UserPrincipal(
                userRepository.findByEmailOrUsername(usernameOrEmail, usernameOrEmail).orElseThrow(()->new UsernameNotFoundException(USER_NOT_FOUND.getMessage()))
        );
    }
}
