package com.apirest.JwtAuthService.controller.dtos.permission;

import java.time.LocalDateTime;

public record PermissionResponse(
        Long permissionId,
        String name,
        String description,
        ResourcePermissionResponse resource,
        String usuReg,
        String usuMod,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
