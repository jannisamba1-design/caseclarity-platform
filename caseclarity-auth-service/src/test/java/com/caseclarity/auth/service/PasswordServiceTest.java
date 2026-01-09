package com.caseclarity.auth.service;

import com.caseclarity.auth.strategy.PasswordEncoderStrategy;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PasswordServiceTest {

    @Mock
    PasswordEncoderStrategy passwordEncoderStrategy;

    @InjectMocks
    private PasswordService passwordService;


    @Test
    void hash_shouldGenerateNonNullHash() {
        String raw = "Password@123";
        when(passwordEncoderStrategy.encode(raw)).thenReturn("");
        String hash = passwordService.hash(raw);

        assertNotNull(hash);
        assertNotEquals(raw, hash);
    }

    @Test
    void verify_shouldReturnTrueForCorrectPassword() {
        String raw = "Password@123";
        when(passwordEncoderStrategy.encode(raw)).thenReturn("");
        String hash = passwordService.hash(raw);
        when(passwordEncoderStrategy.matches(raw, hash)).thenReturn(true);


        boolean result = passwordService.verify(raw, hash);

        assertTrue(result);
    }

    @Test
    void verify_shouldReturnFalseForWrongPassword() {
        String raw = "Password@123";
        String wrong = "WrongPassword";
        when(passwordEncoderStrategy.encode(raw)).thenReturn("");
        String hash = passwordService.hash(raw);
        when(passwordEncoderStrategy.matches(wrong, hash)).thenReturn(false);

        boolean result = passwordService.verify(wrong, hash);

        assertFalse(result);
    }

}
