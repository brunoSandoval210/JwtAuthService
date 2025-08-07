package com.apirest.JwtAuthService.controller.dtos.auth;

import jakarta.validation.constraints.NotBlank;

public record AuthLoginRequest(
        @NotBlank
        String username,
        @NotBlank
        String password ) {
}
