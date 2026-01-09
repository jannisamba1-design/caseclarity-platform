package com.caseclarity.auth.service;

import com.caseclarity.auth.strategy.PasswordEncoderStrategy;
import org.springframework.stereotype.Service;

@Service
public class PasswordService {

    private final PasswordEncoderStrategy encoderStrategy;

    public PasswordService(PasswordEncoderStrategy encoderStrategy) {
        this.encoderStrategy = encoderStrategy;
    }

    public String hash(String rawPassword) {
        return encoderStrategy.encode(rawPassword);
    }

    public boolean verify(String rawPassword, String encodedPassword) {
        return encoderStrategy.matches(rawPassword, encodedPassword);
    }
}
