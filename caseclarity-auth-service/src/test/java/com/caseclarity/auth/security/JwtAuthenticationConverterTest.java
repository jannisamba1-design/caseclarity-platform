package com.caseclarity.auth.security;

import io.jsonwebtoken.impl.DefaultClaims;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import static org.junit.jupiter.api.Assertions.*;

class JwtAuthenticationConverterTest {

    private final JwtAuthenticationConverter converter =
            new JwtAuthenticationConverter();

    @Test
    void shouldConvertClaimsToAuthentication() {

        DefaultClaims claims = new DefaultClaims();
        claims.setSubject("user-id-123");
        claims.put("role", "ADMIN");

        UsernamePasswordAuthenticationToken auth =
                converter.convert(claims);

        assertEquals("user-id-123", auth.getPrincipal());
        assertTrue(
                auth.getAuthorities()
                        .contains(new SimpleGrantedAuthority("ROLE_ADMIN"))
        );
    }
}
