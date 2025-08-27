package com.apirest.JwtAuthService.controller.dtos.role;

import jakarta.validation.constraints.Size;

import java.util.List;

public record PermissionsRoleCreate (
        @Size(min = 1, message = "La lista de permisos no puede estar vacía")
        List<Long> permissionId
){
}
