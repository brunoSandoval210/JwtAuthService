package com.apirest.JwtAuthService.controller.dtos.role;

import jakarta.validation.constraints.Size;


public record RoleUpdateRequest (
        String role,
        @Size(max = 100, message = "La descripción del rol no puede exceder los 100 caracteres")
        String description,
        PermissionsRoleCreate permissions
){
}
