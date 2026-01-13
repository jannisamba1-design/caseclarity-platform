package com.caseclarity.auth.security;

import com.caseclarity.auth.domain.Role;

import java.util.Set;
import java.util.UUID;

public interface TokenFactory {

    String createToken(
            TokenType tokenType,
            UUID userId,
            String email,
            String tenantId,
            Set<Role> roles
    );
}
