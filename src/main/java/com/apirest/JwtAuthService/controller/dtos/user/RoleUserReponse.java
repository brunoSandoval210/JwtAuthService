package com.apirest.JwtAuthService.controller.dtos.user;

import java.util.List;

public record RoleUserReponse(
        String name,
        List<PermissionRoleUserResponse> permissions
) {
}
