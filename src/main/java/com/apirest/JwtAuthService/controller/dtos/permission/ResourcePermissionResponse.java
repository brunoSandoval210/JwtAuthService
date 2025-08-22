package com.apirest.JwtAuthService.controller.dtos.permission;

public record ResourcePermissionResponse(
        Long resourceId,
        String name,
        String description
){
}
