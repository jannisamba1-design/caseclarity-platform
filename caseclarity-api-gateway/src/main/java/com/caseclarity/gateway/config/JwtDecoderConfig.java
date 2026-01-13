package com.caseclarity.gateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Configuration
public class JwtDecoderConfig {

    @Value("${JWT_SECRET}")
    private String base64Secret;

    @Bean
    public ReactiveJwtDecoder reactiveJwtDecoder() {
        byte[] decodedKey = Base64.getDecoder().decode(base64Secret);

        SecretKey key = new SecretKeySpec(decodedKey, "HmacSHA256");

        return NimbusReactiveJwtDecoder.withSecretKey(key).build();
    }
}
