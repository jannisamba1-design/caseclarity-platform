package com.caseclarity.auth.service;

import com.caseclarity.auth.domain.RefreshToken;
import com.caseclarity.auth.domain.Role;
import com.caseclarity.auth.domain.User;
import com.caseclarity.auth.dto.LoginRequest;
import com.caseclarity.auth.dto.SignupRequest;
import com.caseclarity.auth.exception.UserAlreadyExistsException;
import com.caseclarity.auth.repository.RefreshTokenRepository;
import com.caseclarity.auth.repository.UserRepository;
import com.caseclarity.auth.security.RefreshTokenGenerator;
import com.caseclarity.auth.security.TokenFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    PasswordService passwordService;

    @Mock
    TokenFactory tokenFactory;

    @Mock
    RefreshTokenRepository refreshTokenRepository;

    @Mock
    AdminUserService adminUserService;

    @Mock
    RefreshTokenGenerator refreshTokenGenerator;

    @InjectMocks
    AuthService authService;

    @Test
    void signup_success() {
        User user = new User();
        user.setEmail("test@case.com");

        when(userRepository.findByEmail(any()))
                .thenReturn(Mono.empty());
        when(passwordService.hash(any()))
                .thenReturn("hashed");
        when(userRepository.save(any()))
                .thenReturn(Mono.just(user));

        StepVerifier.create(authService.signup(new SignupRequest("test@case.com", "pwd")))
                .expectNextMatches(u -> u.getEmail().equals("test@case.com"))
                .verifyComplete();
    }

    @Test
    void login_success() {
        User user = new User();
        user.setPasswordHash("hashed");
        user.setRole(Role.USER);

        when(userRepository.findByEmail(any()))
                .thenReturn(Mono.just(user));
        when(passwordService.verify(any(), any()))
                .thenReturn(true);
        when(tokenFactory.createToken(any(),any(), any(), any())).thenReturn("access_token");
        when(refreshTokenGenerator.generate()).thenReturn("refresh_token");
        when(refreshTokenRepository.save(any())).thenReturn(Mono.just(new RefreshToken()));

        StepVerifier.create(authService.login(
                        new LoginRequest("a@b.com", "password")))
                .expectNextMatches(res -> {
                    return "access_token".equals(res.getAccessToken())
                            && "refresh_token".equals(res.getRefreshToken());
                })
                .verifyComplete();
    }

    @Test
    void login_invalid_password() {
        User user = new User();
        user.setPasswordHash("hashed");

        when(userRepository.findByEmail(any()))
                .thenReturn(Mono.just(user));
        when(passwordService.verify(any(), any()))
                .thenReturn(false);

        StepVerifier.create(authService.login(
                        new LoginRequest("a@b.com", "wrong")))
                .expectError(IllegalArgumentException.class)
                .verify();
    }

    @Test
    void signup_fails_when_user_exists() {

        when(userRepository.findByEmail(any()))
                .thenReturn(Mono.just(new User()));

        StepVerifier.create(
                        authService.signup(new SignupRequest("test@case.com", "Password@123"))
                )
                .expectError(UserAlreadyExistsException.class)
                .verify();
    }

    @Test
    void login_fails_when_user_not_found() {

        when(userRepository.findByEmail(any()))
                .thenReturn(Mono.empty());

        StepVerifier.create(
                        authService.login(new LoginRequest("x@y.com", "pwd"))
                )
                .expectError(IllegalArgumentException.class)
                .verify();
    }

//    @Test
//    void refresh_fails_when_token_not_found() {
//
//        when(refreshTokenRepository.findByToken(any()))
//                .thenReturn(Mono.empty());
//
//        StepVerifier.create(
//                        authService.refreshAccessToken("invalid")
//                )
//                .expectError(IllegalArgumentException.class)
//                .verify();
//    }

//    @Test
//    void refresh_fails_when_token_expired() {
//
//        RefreshToken token = RefreshToken.builder()
//                .expiresAt(Instant.now().minusSeconds(10))
//                .revoked(false)
//                .build();
//
//        when(refreshTokenRepository.findByToken(any()))
//                .thenReturn(Mono.just(token));
//
//        StepVerifier.create(
//                        authService.refreshAccessToken("expired")
//                )
//                .expectError(IllegalArgumentException.class)
//                .verify();
//    }

//    @Test
//    void refresh_success() {
//
//        RefreshToken token = RefreshToken.builder()
//                .userId(UUID.randomUUID())
//                .expiresAt(Instant.now().plusSeconds(1000))
//                .revoked(false)
//                .build();
//
//        User user = new User();
//        user.setRole(Role.USER);
//
//        when(refreshTokenRepository.findByToken(any()))
//                .thenReturn(Mono.just(token));
//        when(userRepository.findById((UUID) any()))
//                .thenReturn(Mono.just(user));
//        when(tokenFactory.createToken(any(), any(), any(), any()))
//                .thenReturn("new-access-token");
//
//        StepVerifier.create(
//                        authService.refreshAccessToken("valid")
//                )
//                .expectNextMatches(resp ->
//                        resp.getAccessToken().equals("new-access-token")
//                )
//                .verifyComplete();
//    }

}
