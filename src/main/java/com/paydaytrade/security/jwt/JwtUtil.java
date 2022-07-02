package com.paydaytrade.security.jwt;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
public class JwtUtil {

    //token generate

    @Value("${app.jwt-expiration-milliseconds}")
    private int expirationDate;

    @Value("app.jwt-secret")
    private String secretKey;

    public String generateToke(Authentication authentication) {
        Date currentDate = new Date();
        Date expiredDate = new Date(currentDate.getTime() + expirationDate);
        String username = authentication.getName();
        return Jwts.builder()
                .setIssuedAt(new Date())
                .setExpiration(expiredDate)
                .setSubject(username)
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();

    }


    //get username from token

    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
        return claims.getSubject();
    }

    //validate token
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (SignatureException e) {
            log.error("Invalid  JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.error("Invalid  JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }

}
