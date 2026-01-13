package com.caseclarity.auth.repository;

import com.caseclarity.auth.domain.RefreshToken;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface RefreshTokenRepository
        extends ReactiveCrudRepository<RefreshToken, UUID> {

    Mono<RefreshToken> findByToken(String token);

    Flux<RefreshToken> findAllByUserId(UUID userId);

    Mono<Void> deleteByUserId(UUID userId);

    Mono<Boolean> existsByTokenAndRevokedFalse(String token);
}
