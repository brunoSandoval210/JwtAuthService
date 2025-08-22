package com.apirest.JwtAuthService.controller.dtos.role;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

public record RoleCreateRequest (
        @NotBlank(message = "El nombre del rol no puede estar vacío")
        String role,
        @NotBlank(message = "La descripción del rol no puede estar vacía")
        @Size(max = 100, message = "La descripción del rol no puede exceder los 100 caracteres")
        String description,
        @Size(min = 1, message = "La lista de permisos no puede estar vacía")
        List<PermissionsRoleCreate> permissions
){
}
