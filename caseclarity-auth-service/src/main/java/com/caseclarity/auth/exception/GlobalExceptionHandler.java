package com.caseclarity.auth.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserAlreadyExistsException.class)
    public Mono<ResponseEntity<Map<String, Object>>> handleUserAlreadyExists(
            UserAlreadyExistsException ex
    ) {
        return Mono.just(
                ResponseEntity
                        .status(HttpStatus.CONFLICT)
                        .body(Map.of(
                                "timestamp", Instant.now(),
                                "status", 409,
                                "error", "Conflict",
                                "message", ex.getMessage()
                        ))
        );
    }
}
