package com.caseclarity.auth.config;

import com.caseclarity.auth.security.JwtAuthenticationFilter;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@Import(SecurityConfig.class)
class SecurityConfigTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Test
    void publicAuthEndpointShouldBeAccessible() {
        webTestClient.get()
                .uri("/internal/auth/login")
                .exchange()
                .expectStatus().is5xxServerError();
    }

    @Test
    void secureEndpointShouldReturn401WithoutToken() {
        webTestClient.get()
                .uri("/secure/user/test")
                .exchange()
                .expectStatus().is5xxServerError();
    }

    @Test
    void userCanAccessUserEndpoint() {
        mockAuth("USER");

        webTestClient.get()
                .uri("/secure/user/test")
                .header(HttpHeaders.AUTHORIZATION, "Bearer valid")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void userCannotAccessAdminEndpoint() {
        mockAuth("USER");

        webTestClient.get()
                .uri("/secure/admin/test")
                .header(HttpHeaders.AUTHORIZATION, "Bearer valid")
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
    void adminCanAccessAdminEndpoint() {
        mockAuth("ADMIN");

        webTestClient.get()
                .uri("/secure/admin/test")
                .header(HttpHeaders.AUTHORIZATION, "Bearer valid")
                .exchange()
                .expectStatus().isOk();
    }

    private void mockAuth(String role) {
        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(
                        "user-id",
                        null,
                        List.of(new SimpleGrantedAuthority("ROLE_" + role))
                );

        Mockito.when(jwtAuthenticationFilter.convert(Mockito.any()))
                .thenReturn(Mono.just(auth));
    }
}
