//package com.caseclarity.auth.controller;
//
//import com.caseclarity.auth.domain.Role;
//import com.caseclarity.auth.dto.*;
//import com.caseclarity.auth.service.AuthService;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.reactive.server.WebTestClient;
//import reactor.core.publisher.Mono;
//
//import java.util.UUID;
//
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@AutoConfigureWebTestClient
//class AuthControllerTest {
//
//    @Autowired
//    private WebTestClient webTestClient;
//
//    @MockBean
//    private AuthService authService;
//
//    // ---------------- SIGNUP ----------------
//
//    @Test
//    void signupReturns201() {
//
//        SignupRequest request = new SignupRequest(
//                "user@test.com",
//                "Password@123"
//        );
//
//        UserResponse response =
//                UserResponse.builder().build();
//        response.setId(UUID.randomUUID());
//        response.setRole(Role.USER);
//        response.setEmail("user@test.com");
//
//        Mockito.when(authService.signup(Mockito.any()))
//                .thenReturn(Mono.just(response));
//
//        webTestClient.post()
//                .uri("/internal/auth/signup")
//                .contentType(MediaType.APPLICATION_JSON)
//                .bodyValue(request)
//                .exchange()
//                .expectStatus().isCreated()
//                .expectBody(UserResponse.class)
//                .value(r -> {
//                    assert r.getEmail().equals("user@test.com");
//                });
//    }
//
//    // ---------------- LOGIN ----------------
//
//    @Test
//    void loginReturnsAccessToken() {
//
//        LoginRequest request = new LoginRequest(
//                "user@test.com",
//                "Password@123"
//        );
//
//        TokenResponse tokenResponse = TokenResponse.builder().build();
//        tokenResponse.setAccessToken("access-token");
//        tokenResponse.setRefreshToken("refresh-token");
//
//        Mockito.when(authService.login(Mockito.any()))
//                .thenReturn(Mono.just(tokenResponse));
//
//        webTestClient.post()
//                .uri("/internal/auth/login")
//                .contentType(MediaType.APPLICATION_JSON)
//                .bodyValue(request)
//                .exchange()
//                .expectStatus().isOk()
//                .expectBody(TokenResponse.class)
//                .value(token -> {
//                    assert token.getAccessToken().equals("access-token");
//                });
//    }
//
//    // ---------------- REFRESH ----------------
//
//    @Test
//    void refreshReturnsNewAccessToken() {
//
//        RefreshTokenRequest request =
//                new RefreshTokenRequest();
//        request.setRefreshToken("refresh-token");
//
//        TokenResponse response = TokenResponse
//                .builder().accessToken("new-access")
//                .refreshToken( "refresh-token")
//                        .build();
//
//        Mockito.when(authService.refreshAccessToken("refresh-token"))
//                .thenReturn(Mono.just(response));
//
//        webTestClient.post()
//                .uri("/internal/auth/refresh")
//                .contentType(MediaType.APPLICATION_JSON)
//                .bodyValue(request)
//                .exchange()
//                .expectStatus().isOk()
//                .expectBody(TokenResponse.class)
//                .value(r -> {
//                    assert r.getAccessToken().equals("new-access");
//                });
//    }
//
//    // ---------------- SECURED ----------------
//
//    @Test
//    void secureEndpointWithoutAuthReturns401() {
//
//        webTestClient.get()
//                .uri("/internal/auth/secure")
//                .exchange()
//                .expectStatus().isUnauthorized();
//    }
//}
