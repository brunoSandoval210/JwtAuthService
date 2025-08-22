package com.apirest.JwtAuthService.controller.dtos.user;

import java.util.List;

public record UserReponse(
        String username,
        boolean isEnabled,
        boolean accountNoExpired,
        boolean accountNonLocked,
        boolean credentialsNonExpired,
        List<RoleUserReponse> roles
) {

}
