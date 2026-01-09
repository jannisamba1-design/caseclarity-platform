package com.caseclarity.auth.security;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class RefreshTokenGeneratorTest {
    @InjectMocks
    RefreshTokenGenerator tokenGenerator;

    @Test
    void test_generate() {
        var token = tokenGenerator.generate();
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

}