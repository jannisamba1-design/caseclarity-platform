package com.caseclarity.auth.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/secure")
public class SecureController {

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER')")
    public Mono<String> userEndpoint() {
        return Mono.just("User access granted");
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<String> adminEndpoint() {
        return Mono.just("Admin access granted");
    }
}
