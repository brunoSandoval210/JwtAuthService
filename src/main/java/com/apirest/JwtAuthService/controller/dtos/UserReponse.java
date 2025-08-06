package com.apirest.JwtAuthService.controller.dtos;

import java.util.List;

public record UserReponse(
        String username,
        boolean isEnabled,
        boolean accountNoExpired,
        boolean accountNonLocked,
        boolean credentialsNonExpired,
        List<RoleReponse> roles
) {

}
