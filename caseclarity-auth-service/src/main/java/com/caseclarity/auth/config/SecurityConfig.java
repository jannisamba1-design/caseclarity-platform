package com.caseclarity.auth.config;

import com.caseclarity.auth.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

@EnableReactiveMethodSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {

        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers(
                                "/internal/auth/signup",
                                "/internal/auth/login",
                                "/internal/auth/refresh"
                        ).permitAll()
                        .anyExchange().authenticated()
                )
                .authenticationManager(authenticationManager())
                .securityContextRepository(
                        new JwtSecurityContextRepository(jwtAuthenticationFilter)
                )
                .build();
    }

    private ReactiveAuthenticationManager authenticationManager() {
        return authentication -> Mono.just(authentication);
    }
}
