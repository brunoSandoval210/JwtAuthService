package com.apirest.JwtAuthService.controller.dtos.Permission;

public record ResourcePermissionResponse(
        Long resourceId,
        String name,
        String description
){
}
