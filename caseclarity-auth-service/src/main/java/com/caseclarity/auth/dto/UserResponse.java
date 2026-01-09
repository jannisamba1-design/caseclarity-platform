package com.caseclarity.auth.dto;

import com.caseclarity.auth.domain.Role;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
public class UserResponse {

    private UUID id;
    private String email;
    private Role role;
    private String status;
    private Instant createdAt;
}
