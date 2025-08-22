package com.apirest.JwtAuthService.controller.dtos.permission;

import jakarta.validation.constraints.Size;

public record PermissionUpdateRequest(
        String name,
        @Size(max = 100, message = "La descripción del permiso no puede exceder los 100 caracteres")
        String description,
        Long resourceId
) {
}
