package com.caseclarity.auth.controller;

import com.caseclarity.auth.dto.UpdateUserRoleRequest;
import com.caseclarity.auth.service.AdminUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/internal/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final AdminUserService adminUserService;

    @PatchMapping("/{userId}/role")
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<Void> updateUserRole(
            @PathVariable UUID userId,
            @RequestBody UpdateUserRoleRequest request
    ) {
        return adminUserService.updateUserRole(userId, request.getRole());
    }
}
