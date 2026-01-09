package com.caseclarity.auth.security;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter implements ServerAuthenticationConverter {

    private final JwtTokenProvider tokenProvider;
    private final JwtAuthenticationConverter authenticationConverter;

    @Override
    public Mono<org.springframework.security.core.Authentication> convert(ServerWebExchange exchange) {

        String authHeader = exchange.getRequest()
                .getHeaders()
                .getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return Mono.empty();
        }

        String token = authHeader.substring(7);

        return Mono.fromCallable(() -> tokenProvider.validateAndGetClaims(token))
                .map(authenticationConverter::convert);
    }
}
