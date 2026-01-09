package com.caseclarity.auth.strategy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class BCryptPasswordEncoderStrategyTest {

    @Mock
    BCryptPasswordEncoder encoder;

    @InjectMocks
    BCryptPasswordEncoderStrategy encoderStrategy;

    @Test
    void test_encode() {
        String raw = "word";
        var hash = encoderStrategy.encode(raw);
        assertNotNull(hash);
    }

    @Test
    void test_matches() {
        String raw = "word";
        String hash = "hashed";
        var match = encoderStrategy.matches(raw, hash);
        assertFalse(match);
    }
}