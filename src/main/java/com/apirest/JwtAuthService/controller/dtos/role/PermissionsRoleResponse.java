package com.apirest.JwtAuthService.controller.dtos.role;

public record PermissionsRoleResponse (
        Long permissionId,
        String name,
        String description
){
}
