package com.apirest.JwtAuthService.controller.dtos;

import java.util.List;

public record RoleReponse(
        String name,
        List<PermissionResponse> permissions
) {
}
