package com.caseclarity.auth.security;

import com.caseclarity.auth.domain.Role;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

@Component
public class JwtTokenFactory implements TokenFactory {

    private final SecretKey secretKey;
    private final long accessTokenExpiry;
    private final long refreshTokenExpiry;

    public JwtTokenFactory(
            @Value("${jwt.secret}") String base64Secret,
            @Value("${jwt.access-expiration}") long accessTokenExpiry,
            @Value("${jwt.refresh-expiration}") long refreshTokenExpiry
    ) {
        this.secretKey = Keys.hmacShaKeyFor(
                Base64.getDecoder().decode(base64Secret)
        );
        this.accessTokenExpiry = accessTokenExpiry;
        this.refreshTokenExpiry = refreshTokenExpiry;
    }

    @Override
    public String createToken(
            TokenType tokenType,
            UUID userId,
            String email,
            String tenantId,
            Set<Role> roles
    ) {

        long expiry = resolveExpiry(tokenType);

        return Jwts.builder()
                .setSubject(userId.toString())
                .claim("email", email)
                .claim("tenantId", tenantId)

                // 🔑 RBAC
                .claim(
                        "roles",
                        roles.stream().map(Enum::name).toList()
                )

                // 🔑 Scopes (OAuth2-compatible)
                .claim(
                        "scope",
                        resolveScopes(roles)
                )

                .claim("type", tokenType.name())
                .setIssuedAt(new Date())
                .setExpiration(
                        new Date(System.currentTimeMillis() + expiry * 1000)
                )
                .signWith(secretKey)
                .compact();
    }

    private long resolveExpiry(TokenType tokenType) {
        return tokenType == TokenType.ACCESS
                ? accessTokenExpiry
                : refreshTokenExpiry;
    }

    private String resolveScopes(Set<Role> roles) {
        if (roles.contains(Role.ADMIN)) {
            return "evidence:read evidence:write";
        }
        return "evidence:read";
    }
}
