//package com.caseclarity.auth.controller;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.test.web.reactive.server.WebTestClient;
//
//import java.util.List;
//
//import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.mockAuthentication;
//
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@AutoConfigureWebTestClient
//class SecureControllerTest {
//
//    @Autowired
//    private WebTestClient webTestClient;
//
//    // -------- USER ACCESS --------
//
//    @Test
//    void userCanAccessUserEndpoint() {
//
//        UsernamePasswordAuthenticationToken userAuth =
//                new UsernamePasswordAuthenticationToken(
//                        "user-id",
//                        null,
//                        List.of(new SimpleGrantedAuthority("ROLE_USER"))
//                );
//
//        webTestClient
//                .mutateWith(mockAuthentication(userAuth))
//                .get()
//                .uri("/secure/user")
//                .exchange()
//                .expectStatus().isOk()
//                .expectBody(String.class)
//                .isEqualTo("User access granted");
//    }
//
//    // -------- USER BLOCKED FROM ADMIN --------
//
//    @Test
//    void userCannotAccessAdminEndpoint() {
//
//        UsernamePasswordAuthenticationToken userAuth =
//                new UsernamePasswordAuthenticationToken(
//                        "user-id",
//                        null,
//                        List.of(new SimpleGrantedAuthority("ROLE_USER"))
//                );
//
//        webTestClient
//                .mutateWith(mockAuthentication(userAuth))
//                .get()
//                .uri("/secure/admin")
//                .exchange()
//                .expectStatus().isForbidden();
//    }
//
//    // -------- ADMIN ACCESS --------
//
//    @Test
//    void adminCanAccessAdminEndpoint() {
//
//        UsernamePasswordAuthenticationToken adminAuth =
//                new UsernamePasswordAuthenticationToken(
//                        "admin-id",
//                        null,
//                        List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))
//                );
//
//        webTestClient
//                .mutateWith(mockAuthentication(adminAuth))
//                .get()
//                .uri("/secure/admin")
//                .exchange()
//                .expectStatus().isOk()
//                .expectBody(String.class)
//                .isEqualTo("Admin access granted");
//    }
//
//    // -------- UNAUTHENTICATED --------
//
//    @Test
//    void unauthenticatedAccessIsRejected() {
//
//        webTestClient
//                .get()
//                .uri("/secure/user")
//                .exchange()
//                .expectStatus().isUnauthorized();
//    }
//}
