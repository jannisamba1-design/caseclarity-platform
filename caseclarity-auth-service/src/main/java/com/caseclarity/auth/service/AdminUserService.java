package com.caseclarity.auth.service;

import com.caseclarity.auth.domain.Role;
import com.caseclarity.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminUserService {

    private final UserRepository userRepository;

    public Mono<Void> updateUserRole(UUID userId, Role newRole) {

        return userRepository.findById(userId)
                .switchIfEmpty(Mono.error(
                        new IllegalArgumentException("User not found")
                ))
                .flatMap(user -> {
                    user.setRole(newRole);
                    return userRepository.save(user);
                })
                .then();
    }
}
