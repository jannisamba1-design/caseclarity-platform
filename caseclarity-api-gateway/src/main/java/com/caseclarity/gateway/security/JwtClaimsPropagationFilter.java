package com.caseclarity.gateway.security;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Order(Ordered.LOWEST_PRECEDENCE)
public class JwtClaimsPropagationFilter implements GlobalFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        return exchange.getPrincipal()
                .filter(p -> p instanceof JwtAuthenticationToken)
                .cast(JwtAuthenticationToken.class)
                .map(jwtAuth -> {
                    Jwt jwt = jwtAuth.getToken();

                    ServerHttpRequest mutatedRequest = exchange.getRequest()
                            .mutate()
                            .header("X-User-Id", jwt.getSubject())
                            .header("X-Tenant-Id", jwt.getClaimAsString("tenantId"))
                            .header(
                                    "X-Roles",
                                    String.join(",", jwt.getClaimAsStringList("roles"))
                            )
                            .header(
                                    "X-Scopes",
                                    jwt.getClaimAsString("scope")
                            )
                            .build();

                    return exchange.mutate().request(mutatedRequest).build();
                })
                .defaultIfEmpty(exchange)
                .flatMap(chain::filter);
    }
}
