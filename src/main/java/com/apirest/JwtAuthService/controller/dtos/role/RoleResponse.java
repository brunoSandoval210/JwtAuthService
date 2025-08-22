package com.apirest.JwtAuthService.controller.dtos.role;

import java.time.LocalDateTime;
import java.util.List;

public record RoleResponse(
        Long roleId,
        String role,
        String description,
        List<PermissionsRoleResponse> permissions,
        String usuReg,
        String usuMod,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
