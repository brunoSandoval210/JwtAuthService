package com.apirest.JwtAuthService.controller.dtos.user;

import java.util.List;

public record RoleReponse(
        String name,
        List<PermissionResponse> permissions
) {
}
