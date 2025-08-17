package com.apirest.JwtAuthService.controller.dtos.user;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

public record UserUpdateRequest(
        @NotBlank String password,
        @Valid UserRolesCreateRequest roleCreateRequest
){
}
