package com.caseclarity.auth.controller;

import com.caseclarity.auth.domain.Role;
import com.caseclarity.auth.dto.UpdateUserRoleRequest;
import com.caseclarity.auth.service.AdminUserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.mockAuthentication;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class AdminUserControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private AdminUserService adminUserService;

    // ---------------- ADMIN SUCCESS ----------------

    @Test
    void adminCanUpdateUserRole() {

        UUID userId = UUID.randomUUID();
        UpdateUserRoleRequest request = new UpdateUserRoleRequest(Role.USER);

        Mockito.when(adminUserService.updateUserRole(userId, Role.USER))
                .thenReturn(Mono.empty());

        UsernamePasswordAuthenticationToken adminAuth =
                new UsernamePasswordAuthenticationToken(
                        "admin-id",
                        null,
                        List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))
                );

        webTestClient
                .mutateWith(mockAuthentication(adminAuth))
                .patch()
                .uri("/internal/admin/users/{id}/role", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk();

        Mockito.verify(adminUserService)
                .updateUserRole(userId, Role.USER);
    }

    // ---------------- USER FORBIDDEN ----------------

    @Test
    void userCannotUpdateUserRole() {

        UUID userId = UUID.randomUUID();
        UpdateUserRoleRequest request = new UpdateUserRoleRequest(Role.ADMIN);

        UsernamePasswordAuthenticationToken userAuth =
                new UsernamePasswordAuthenticationToken(
                        "user-id",
                        null,
                        List.of(new SimpleGrantedAuthority("ROLE_USER"))
                );

        webTestClient
                .mutateWith(mockAuthentication(userAuth))
                .patch()
                .uri("/internal/admin/users/{id}/role", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isForbidden();

        Mockito.verifyNoInteractions(adminUserService);
    }

    // ---------------- UNAUTHENTICATED ----------------

    @Test
    void unauthenticatedRequestIsUnauthorized() {

        UUID userId = UUID.randomUUID();
        UpdateUserRoleRequest request = new UpdateUserRoleRequest(Role.ADMIN);

        webTestClient
                .patch()
                .uri("/internal/admin/users/{id}/role", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isUnauthorized();

        Mockito.verifyNoInteractions(adminUserService);
    }
}
