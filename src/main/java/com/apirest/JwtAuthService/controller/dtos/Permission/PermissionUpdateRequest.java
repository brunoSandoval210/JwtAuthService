package com.apirest.JwtAuthService.controller.dtos.Permission;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;

@Valid
public record PermissionUpdateRequest(
        String name,
        @Size(max = 100, message = "La descripción del permiso no puede exceder los 100 caracteres")
        String description,
        Long resourceId
) {
}
