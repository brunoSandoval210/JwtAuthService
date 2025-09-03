package com.apirest.JwtAuthService.auth;

import com.apirest.JwtAuthService.persistence.enums.ErrorCodeEnum;
import com.apirest.JwtAuthService.controller.dtos.response.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        // Determina el tipo de error
        ErrorCodeEnum errorCode = determineErrorCode(authException);

        // Construye la respuesta de error
        ErrorResponse errorResponse = new ErrorResponse(
                errorCode.name(),
                getErrorMessage(authException, errorCode),
                errorCode.getCode()
        );

        // Configura la respuesta HTTP
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(errorCode
                .getHttpStatus().value());
        objectMapper.writeValue(response.getWriter(), errorResponse);
    }

    private ErrorCodeEnum determineErrorCode(AuthenticationException ex) {
        Throwable cause = ex.getCause();

        if (ex instanceof BadCredentialsException) {
            return ErrorCodeEnum.BAD_CREDENTIALS;
        }
        else if (cause instanceof ExpiredJwtException) {
            return ErrorCodeEnum.EXPIRED_TOKEN;
        }
        else if (cause instanceof JwtException) {
            return ErrorCodeEnum.INVALID_TOKEN;
        }
        else if (ex instanceof InsufficientAuthenticationException) {
            return ErrorCodeEnum.UNAUTHORIZED;
        }
        return ErrorCodeEnum.AUTHENTICATION_FAILED;
    }

    private String getErrorMessage(AuthenticationException ex, ErrorCodeEnum errorCode) {
        return switch (errorCode) {
            case BAD_CREDENTIALS -> "Usuario o contraseña incorrectos";
            case EXPIRED_TOKEN -> "El token JWT ha expirado";
            case INVALID_TOKEN -> "Token JWT inválido";
            default -> "Autenticación requerida";
        };
    }
}