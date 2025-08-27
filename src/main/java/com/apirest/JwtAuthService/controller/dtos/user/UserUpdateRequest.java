package com.apirest.JwtAuthService.controller.dtos.user;


import jakarta.validation.constraints.NotBlank;

public record UserUpdateRequest(
        @NotBlank String password,
        UserRolesCreateRequest roleCreateRequest
){
}
