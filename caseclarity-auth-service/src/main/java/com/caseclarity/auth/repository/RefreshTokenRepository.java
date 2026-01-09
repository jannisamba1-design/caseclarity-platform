package com.caseclarity.auth.repository;

import com.caseclarity.auth.domain.RefreshToken;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import java.util.UUID;
import reactor.core.publisher.Mono;

public interface RefreshTokenRepository
        extends ReactiveCrudRepository<RefreshToken, UUID> {

    Mono<RefreshToken> findByToken(String token);
}
