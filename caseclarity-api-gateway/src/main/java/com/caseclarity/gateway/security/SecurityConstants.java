package com.caseclarity.gateway.security;

public final class SecurityConstants {

    public static final String[] PUBLIC_ENDPOINTS = {
            "/auth/internal/auth/signup",
            "/auth/internal/auth/login",
            "/auth/internal/auth/refresh",
            "/actuator/**"
    };

    private SecurityConstants() {}
}

