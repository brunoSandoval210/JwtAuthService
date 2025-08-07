package com.apirest.JwtAuthService.controller.dtos.user;

import jakarta.validation.constraints.Size;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Validated
public record UserRolesCreateRequest(
        @Size(min = 1, max = 3, message = "Un usuario puede tener como máximo 3 roles y 1 como mínimo")
        List<String> roleNames
) {
}
