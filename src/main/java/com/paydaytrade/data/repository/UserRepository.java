package com.paydaytrade.data.repository;

import com.paydaytrade.data.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByEmailOrUsername(String email, String username);
    Boolean existsByEmail(String email);
    Boolean existsByUsername(String username);
    User findByActivationCode(String activationCode);
    Optional<User> findByEmail(String email);

    User getByEmail(String email);

    Optional<User> findBySixDigitCode(String sixDigitCode);
}
