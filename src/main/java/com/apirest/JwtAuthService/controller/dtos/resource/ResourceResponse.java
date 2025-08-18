package com.apirest.JwtAuthService.controller.dtos.resource;

import com.apirest.JwtAuthService.controller.dtos.user.PermissionRoleUserResponse;

import java.time.LocalDateTime;
import java.util.List;

public record ResourceResponse (
    Long resourceId,
    String name,
    String description,
    List<PermissionRoleUserResponse> permissions,
    String usuReg,
    String usuMod,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
){
}
