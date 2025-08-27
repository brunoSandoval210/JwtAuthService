package com.apirest.JwtAuthService.controller.dtos.user;

import java.util.List;

public record RoleUserReponse(
        Long roleId,
        String name,
        List<PermissionRoleUserResponse> permissions
) {
}
