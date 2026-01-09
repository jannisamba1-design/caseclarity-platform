package com.caseclarity.auth.security;

import java.util.UUID;

public interface TokenFactory {

    String createToken(
            TokenType tokenType,
            UUID userId,
            String email,
            String role
    );
}
