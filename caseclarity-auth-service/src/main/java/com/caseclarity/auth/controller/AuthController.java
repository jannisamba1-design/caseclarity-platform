package com.caseclarity.auth.controller;

import com.caseclarity.auth.dto.*;
import com.caseclarity.auth.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/internal/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<UserResponse> signup(@Valid @RequestBody SignupRequest request) {
        return authService.signup(request);
    }

    @PostMapping("/login")
    public Mono<ResponseEntity<TokenResponse>> login(
            @RequestBody LoginRequest request
    ) {
        return authService.login(request)
                .map(ResponseEntity::ok);
    }

    @PostMapping("/refresh")
    public Mono<TokenResponse> refresh(
            @RequestBody RefreshTokenRequest request
    ) {
        return authService.refreshAccessToken(request.getRefreshToken());
    }

    @PostMapping("/logout")
    public Mono<ResponseEntity<Void>> logout(
            @RequestHeader("X-User-Id") UUID userId
    ) {
        return authService.logout(userId)
                .thenReturn(ResponseEntity.noContent().build());
    }

    @GetMapping("/secure")
    public Mono<String> secure(
            @RequestHeader("X-User-Id") UUID userId,
            @RequestHeader("X-User-Role") String role
    ) {
        return Mono.just("Access Granted to " + role + " user " + userId);
    }
}
