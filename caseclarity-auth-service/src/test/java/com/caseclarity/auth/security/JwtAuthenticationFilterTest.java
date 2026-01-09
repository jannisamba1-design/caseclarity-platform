package com.caseclarity.auth.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.impl.DefaultClaims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.server.ServerWebExchange;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

class JwtAuthenticationFilterTest {

    private JwtTokenProvider tokenProvider;
    private JwtAuthenticationConverter authenticationConverter;
    private JwtAuthenticationFilter filter;

    @BeforeEach
    void setup() {
        tokenProvider = mock(JwtTokenProvider.class);
        authenticationConverter = mock(JwtAuthenticationConverter.class);
        filter = new JwtAuthenticationFilter(tokenProvider, authenticationConverter);
    }

    // ----------------------------------------------------------------
    // Case 1: Authorization header missing
    // ----------------------------------------------------------------
    @Test
    void shouldReturnEmptyWhenAuthorizationHeaderIsMissing() {
        ServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest.get("/secure").build()
        );

        StepVerifier.create(filter.convert(exchange))
                .verifyComplete();

        verifyNoInteractions(tokenProvider, authenticationConverter);
    }

    // ----------------------------------------------------------------
    // Case 2: Authorization header does not start with Bearer
    // ----------------------------------------------------------------
    @Test
    void shouldReturnEmptyWhenAuthorizationHeaderIsInvalid() {
        ServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest.get("/secure")
                        .header(HttpHeaders.AUTHORIZATION, "InvalidToken")
                        .build()
        );

        StepVerifier.create(filter.convert(exchange))
                .verifyComplete();

        verifyNoInteractions(tokenProvider, authenticationConverter);
    }

    // ----------------------------------------------------------------
    // Case 3: Invalid JWT (provider throws exception)
    // ----------------------------------------------------------------
    @Test
    void shouldPropagateErrorWhenTokenIsInvalid() {
        ServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest.get("/secure")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer bad.token")
                        .build()
        );

        when(tokenProvider.validateAndGetClaims("bad.token"))
                .thenThrow(new RuntimeException("Invalid JWT"));

        StepVerifier.create(filter.convert(exchange))
                .expectError(RuntimeException.class)
                .verify();

        verify(tokenProvider).validateAndGetClaims("bad.token");
        verifyNoInteractions(authenticationConverter);
    }

    // ----------------------------------------------------------------
    // Case 4: Valid JWT → Authentication returned
    // ----------------------------------------------------------------
    @Test
    void shouldReturnAuthenticationWhenTokenIsValid() {
        ServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest.get("/secure")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer valid.token")
                        .build()
        );

        Claims claims = new DefaultClaims();
        claims.setSubject("user-id");
        claims.put("role", "USER");

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken("user-id", null);

        when(tokenProvider.validateAndGetClaims("valid.token"))
                .thenReturn(claims);
        when(authenticationConverter.convert(claims))
                .thenReturn(authentication);

        StepVerifier.create(filter.convert(exchange))
                .expectNext(authentication)
                .verifyComplete();

        verify(tokenProvider).validateAndGetClaims("valid.token");
        verify(authenticationConverter).convert(claims);
    }
}
