package com.apirest.JwtAuthService.util.exception;

public record ErrorResponse (
        String error,
        String message,
        boolean success
){
}
