package com.apirest.JwtAuthService.controller.dtos.permission;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PermissionCreateRequest(
        @NotBlank(message = "El nombre del permiso no puede estar vacío")
        String name,
        @NotBlank(message = "La descripción del permiso no puede estar vacía")
        @Size(max = 100, message = "La descripción del permiso no puede exceder los 100 caracteres")
        String description,
        Long resourceId
) {
}
