package com.caseclarity.gateway.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;

@Component
public class JwtTokenValidator {

    private final SecretKey secretKey;

    public JwtTokenValidator(@Value("${jwt.secret}") String base64Secret) {

        if (base64Secret == null || base64Secret.isBlank()) {
            throw new IllegalStateException("JWT secret is missing. Set jwt.secret");
        }

        byte[] decodedKey;
        try {
            decodedKey = Base64.getDecoder().decode(base64Secret);
        } catch (IllegalArgumentException ex) {
            throw new IllegalStateException("JWT secret must be Base64 encoded", ex);
        }

        if (decodedKey.length < 32) { // 256 bits
            throw new IllegalStateException("JWT secret must be at least 256 bits");
        }

        this.secretKey = Keys.hmacShaKeyFor(decodedKey);
    }

    public Claims validateAndGetClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
