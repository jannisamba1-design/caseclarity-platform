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

class JwtTokenFactoryTest {

    private JwtTokenFactory jwtTokenFactory;
    private SecretKey secretKey;

    private static final long ACCESS_EXPIRY = 3600;   // 1 hour
    private static final long REFRESH_EXPIRY = 86400; // 1 day

    @BeforeEach
    void setup() {
        secretKey = Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS256);
        String base64Secret =
                Base64.getEncoder().encodeToString(secretKey.getEncoded());

        jwtTokenFactory = new JwtTokenFactory(
                base64Secret,
                ACCESS_EXPIRY,
                REFRESH_EXPIRY
        );
    }

    // ---------- ACCESS TOKEN TESTS ----------

    @Test
    void shouldCreateAccessTokenWithCorrectClaims() {
        UUID userId = UUID.randomUUID();

        String token = jwtTokenFactory.createToken(
                TokenType.ACCESS,
                userId,
                "user@caseclarity.com",
                "USER"
        );

        Claims claims = parseToken(token);

        assertEquals(userId.toString(), claims.getSubject());
        assertEquals("user@caseclarity.com", claims.get("email"));
        assertEquals("USER", claims.get("role"));
        assertEquals("ACCESS", claims.get("type"));
    }

    @Test
    void accessTokenShouldExpireWithinConfiguredTime() {
        UUID userId = UUID.randomUUID();
        long now = System.currentTimeMillis();

        String token = jwtTokenFactory.createToken(
                TokenType.ACCESS,
                userId,
                "user@caseclarity.com",
                "USER"
        );

        Claims claims = parseToken(token);
        Date expiration = claims.getExpiration();

        long diffSeconds = (expiration.getTime() - now) / 1000;

        assertTrue(diffSeconds <= ACCESS_EXPIRY);
        assertTrue(diffSeconds > ACCESS_EXPIRY - 5); // buffer for execution time
    }

    // ---------- REFRESH TOKEN TESTS ----------

    @Test
    void shouldCreateRefreshTokenWithCorrectType() {
        UUID userId = UUID.randomUUID();

        String token = jwtTokenFactory.createToken(
                TokenType.REFRESH,
                userId,
                "user@caseclarity.com",
                "USER"
        );

        Claims claims = parseToken(token);

        assertEquals("REFRESH", claims.get("type"));
    }

    @Test
    void refreshTokenShouldHaveLongerExpiryThanAccessToken() {
        UUID userId = UUID.randomUUID();

        String accessToken = jwtTokenFactory.createToken(
                TokenType.ACCESS,
                userId,
                "user@caseclarity.com",
                "USER"
        );

        String refreshToken = jwtTokenFactory.createToken(
                TokenType.REFRESH,
                userId,
                "user@caseclarity.com",
                "USER"
        );

        Date accessExpiry = parseToken(accessToken).getExpiration();
        Date refreshExpiry = parseToken(refreshToken).getExpiration();

        assertTrue(refreshExpiry.after(accessExpiry));
    }

    // ---------- SIGNATURE & VALIDITY TESTS ----------

    @Test
    void generatedTokenShouldBeSignedAndParsable() {
        UUID userId = UUID.randomUUID();

        String token = jwtTokenFactory.createToken(
                TokenType.ACCESS,
                userId,
                "user@caseclarity.com",
                "USER"
        );

        Claims claims = parseToken(token);

        assertNotNull(claims);
        assertNotNull(claims.getIssuedAt());
        assertNotNull(claims.getExpiration());
    }

    // ---------- HELPER METHOD ----------

    private Claims parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
