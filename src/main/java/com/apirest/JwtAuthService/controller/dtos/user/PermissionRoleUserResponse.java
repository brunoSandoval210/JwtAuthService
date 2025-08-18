package com.apirest.JwtAuthService.controller.dtos.user;

public record PermissionRoleUserResponse(
        Long permissionId,
        String name,
        String description
){
}
