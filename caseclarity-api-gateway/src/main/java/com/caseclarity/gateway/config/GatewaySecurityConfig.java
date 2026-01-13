//package com.caseclarity.gateway.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.annotation.Order;
//import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
//import org.springframework.security.config.web.server.ServerHttpSecurity;
//import org.springframework.security.web.server.SecurityWebFilterChain;
//import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;
//
//@Configuration
//@EnableWebFluxSecurity
//public class GatewaySecurityConfig {
//
//    /**
//     * PUBLIC endpoints — Auth surface
//     */
//    @Bean
//    @Order(1)
//    public SecurityWebFilterChain authChain(ServerHttpSecurity http) {
//        return http
//                .securityMatcher(
//                        ServerWebExchangeMatchers.pathMatchers("/auth/**")
//                )
//                .csrf(ServerHttpSecurity.CsrfSpec::disable)
//                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
//                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
//                .authorizeExchange(ex -> ex
//                        .anyExchange().permitAll()
//                )
//                .build();
//    }
//
//    /**
//     * EVERYTHING ELSE — locked down for now
//     */
//    @Bean
//    @Order(2)
//    public SecurityWebFilterChain denyAllChain(ServerHttpSecurity http) {
//        return http
//                .csrf(ServerHttpSecurity.CsrfSpec::disable)
//                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
//                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
//                .authorizeExchange(ex -> ex
//                        .anyExchange().denyAll()
//                )
//                .build();
//    }
//}
