package com.caseclarity.gateway.routes;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayRoutesConfig {

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("auth-service", r -> r
                        .path("/auth/{segment}")
                        .or()
                        .path("/auth/{segment}/**")
                        .filters(f -> f
                                .rewritePath(
                                        "/auth/(?<segment>.*)",
                                        "/internal/auth/${segment}"
                                )
                        )
                        .uri("http://caseclarity-auth-service:8080")
                )
                .build();
    }
}