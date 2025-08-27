package com.apirest.JwtAuthService.controller.dtos.user;

import java.util.List;

public record UserReponse(
        Long userId,
        String username,
        boolean isEnabled,
        boolean accountNoExpired,
        boolean accountNonLocked,
        boolean credentialsNonExpired,
        List<RoleUserReponse> roles
) {

}
