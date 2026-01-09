package com.caseclarity.auth.controller;

import com.caseclarity.auth.dto.*;
import com.caseclarity.auth.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

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

    @GetMapping("/secure")
    public Mono<String> secure() {
        return Mono.just("Access Granted");
    }

    @PostMapping("/refresh")
    public Mono<TokenResponse> refresh(
            @RequestBody RefreshTokenRequest request
    ) {
        return authService.refreshAccessToken(request.getRefreshToken());
    }

}
