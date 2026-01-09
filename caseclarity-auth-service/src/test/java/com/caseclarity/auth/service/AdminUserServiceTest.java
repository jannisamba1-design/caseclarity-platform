package com.caseclarity.auth.service;

import com.caseclarity.auth.domain.Role;
import com.caseclarity.auth.domain.User;
import com.caseclarity.auth.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdminUserServiceTest {
    @Mock
    UserRepository userRepository;

    @InjectMocks
    AdminUserService adminUserService;

    @Test
    void admin_updates_user_role() {

        User user = new User();
        user.setRole(Role.ADMIN);

        when(userRepository.findById((UUID) any()))
                .thenReturn(Mono.just(user));
        when(userRepository.save(any()))
                .thenReturn(Mono.just(user));

        StepVerifier.create(
                        adminUserService.updateUserRole(UUID.randomUUID(), Role.ADMIN)
                )
                .verifyComplete();

        assertEquals(Role.ADMIN, user.getRole());
    }

}