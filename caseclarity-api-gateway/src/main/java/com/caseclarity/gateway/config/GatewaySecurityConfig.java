package com.caseclarity.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@EnableWebFluxSecurity
@Configuration
public class GatewaySecurityConfig {

    @Bean
    SecurityWebFilterChain security(ServerHttpSecurity http) {

        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(ex -> ex

                        // Public
                        .pathMatchers("/auth/**").permitAll()

                        // Evidence service authorization
                        .pathMatchers(HttpMethod.GET, "/evidence/**")
                        .hasAuthority("SCOPE_evidence:read")

                        .pathMatchers(HttpMethod.POST, "/evidence/**")
                        .hasAuthority("SCOPE_evidence:write")

                        .pathMatchers(HttpMethod.PUT, "/evidence/**")
                        .hasAuthority("SCOPE_evidence:write")

                        // Everything else
                        .anyExchange().authenticated()
                )
                .oauth2ResourceServer(oauth2 ->
                        oauth2.jwt(jwtSpec -> {}))
                .build();
    }
}
