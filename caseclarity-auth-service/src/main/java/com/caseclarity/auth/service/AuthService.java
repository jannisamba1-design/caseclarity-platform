package com.caseclarity.auth.service;

import com.caseclarity.auth.domain.RefreshToken;
import com.caseclarity.auth.domain.Role;
import com.caseclarity.auth.domain.User;
import com.caseclarity.auth.dto.LoginRequest;
import com.caseclarity.auth.dto.SignupRequest;
import com.caseclarity.auth.dto.TokenResponse;
import com.caseclarity.auth.dto.UserResponse;
import com.caseclarity.auth.exception.UserAlreadyExistsException;
import com.caseclarity.auth.repository.RefreshTokenRepository;
import com.caseclarity.auth.repository.UserRepository;
import com.caseclarity.auth.security.RefreshTokenGenerator;
import com.caseclarity.auth.security.TokenFactory;
import com.caseclarity.auth.security.TokenType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordService passwordService;
    private final TokenFactory tokenFactory;
    private final RefreshTokenGenerator refreshTokenGenerator;
    private final RefreshTokenRepository refreshTokenRepository;
    private final RefreshTokenService refreshTokenService;

    @Value("${jwt.refresh-expiration}") long refreshTokenExpiry;

    public Mono<TokenResponse> login(LoginRequest request) {

        return userRepository.findByEmail(request.getEmail())
                .switchIfEmpty(
                        Mono.error(new IllegalArgumentException("Invalid credentials"))
                )
                .flatMap(user -> {
                    if (!passwordService.verify(
                            request.getPassword(),
                            user.getPasswordHash()
                    )) {
                        return Mono.error(new IllegalArgumentException("Invalid credentials"));
                    }

                    String accessToken = tokenFactory.createToken(
                            TokenType.ACCESS,
                            user.getId(),
                            user.getEmail(),
                            user.getRole().toString()
                    );

                    String refreshTokenValue = refreshTokenGenerator.generate();

                    RefreshToken refreshToken = RefreshToken.builder()
                            .userId(user.getId())
                            .token(refreshTokenValue)
                            .expiresAt(Instant.now().plus(30, ChronoUnit.DAYS))
                            .revoked(false)
                            .build();

                    return refreshTokenRepository.save(refreshToken)
                            .map(saved ->
                                    TokenResponse.builder()
                                            .accessToken(accessToken)
                                            .refreshToken(refreshTokenValue)
                                            .tokenType("Bearer")
                                            .expiresIn(3600)
                                            .build()
                            );
                });
    }


    @Transactional
    public Mono<UserResponse> signup(SignupRequest request) {

        return userRepository.findByEmail(request.getEmail())
                .flatMap(existingUser ->
                        Mono.<User>error(new UserAlreadyExistsException("User already exists"))
                )
                .switchIfEmpty(
                        Mono.defer(() -> {
                            User user = User.builder()
                                    .email(request.getEmail())
                                    .passwordHash(passwordService.hash(request.getPassword()))
                                    .role(Role.USER)
                                    .status("ACTIVE")
                                    .createdAt(Instant.now())
                                    .updatedAt(Instant.now())
                                    .build();

                            return userRepository.save(user);
                        })
                )
                .map(savedUser ->
                        UserResponse.builder()
                                .id(savedUser.getId())
                                .email(savedUser.getEmail())
                                .role(savedUser.getRole())
                                .status(savedUser.getStatus())
                                .createdAt(savedUser.getCreatedAt())
                                .build()
                );
    }

    @Transactional
    public Mono<TokenResponse> refreshAccessToken(String refreshToken) {

        return refreshTokenService.validate(refreshToken)
                .flatMap(oldToken -> {

                    String newAccessToken = tokenFactory.createToken(
                            TokenType.ACCESS,
                            oldToken.getUserId(),
                            null,
                            null
                    );

                    String newRefreshToken = tokenFactory.createToken(
                            TokenType.REFRESH,
                            oldToken.getUserId(),
                            null,
                            null
                    );

                    Instant refreshExpiry = Instant.now().plusSeconds(refreshTokenExpiry);

                    return refreshTokenService.revoke(oldToken)
                            .then(refreshTokenService.create(
                                    oldToken.getUserId(),
                                    newRefreshToken,
                                    refreshExpiry
                            ))
                            .thenReturn(
                                    TokenResponse.builder()
                                            .accessToken(newAccessToken)
                                            .refreshToken(newRefreshToken)
                                            .tokenType("Bearer")
                                            .expiresIn(3600)
                                            .build()
                            );
                });
    }

}
