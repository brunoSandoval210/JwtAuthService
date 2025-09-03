package com.apirest.JwtAuthService.controller.dtos.response;

public record ErrorResponse (
        String error,
        String message,
        int status
){
}
