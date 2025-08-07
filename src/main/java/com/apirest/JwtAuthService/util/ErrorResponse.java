package com.apirest.JwtAuthService.util;

public record ErrorResponse (
        String error,
        String message,
        int status
){
}
