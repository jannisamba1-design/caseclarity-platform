package com.caseclarity.auth.service;

import com.caseclarity.auth.domain.RefreshToken;
import com.caseclarity.auth.exception.InvalidRefreshTokenException;
import com.caseclarity.auth.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository repository;

    public Mono<RefreshToken> validate(String token) {
        return repository.findByToken(token)
                .filter(rt -> !rt.isRevoked())
                .filter(rt -> rt.getExpiresAt().isAfter(Instant.now()))
                .switchIfEmpty(Mono.error(
                        new InvalidRefreshTokenException("Invalid or expired refresh token")
                ));
    }

    public Mono<Void> revoke(RefreshToken token) {
        token.setRevoked(true);
        return repository.save(token).then();
    }

    public Mono<RefreshToken> create(UUID userId, String token, Instant expiresAt) {
        return repository.save(
                RefreshToken.builder()
                        .userId(userId)
                        .token(token)
                        .expiresAt(expiresAt)
                        .revoked(false)
                        .build()
        );
    }
}
