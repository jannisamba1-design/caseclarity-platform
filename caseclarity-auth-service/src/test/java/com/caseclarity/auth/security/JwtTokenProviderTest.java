package com.caseclarity.auth.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class JwtTokenProviderTest {

    private JwtTokenProvider provider;
    private SecretKey secretKey;

    @BeforeEach
    void setup() {
        secretKey = Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS256);
        String base64Secret = Base64.getEncoder().encodeToString(secretKey.getEncoded());
        provider = new JwtTokenProvider(base64Secret);
    }

    @Test
    void shouldValidateValidToken() {
        String token = Jwts.builder()
                .setSubject(UUID.randomUUID().toString())
                .setExpiration(new Date(System.currentTimeMillis() + 10000))
                .signWith(secretKey)
                .compact();

        Claims claims = provider.validateAndGetClaims(token);

        assertNotNull(claims);
        assertNotNull(claims.getSubject());
    }

    @Test
    void shouldFailForExpiredToken() {
        String token = Jwts.builder()
                .setSubject("user")
                .setExpiration(new Date(System.currentTimeMillis() - 1000))
                .signWith(secretKey)
                .compact();

        assertThrows(
                Exception.class,
                () -> provider.validateAndGetClaims(token)
        );
    }

    @Test
    void shouldFailForInvalidToken() {
        assertThrows(
                Exception.class,
                () -> provider.validateAndGetClaims("invalid.token.value")
        );
    }
}
