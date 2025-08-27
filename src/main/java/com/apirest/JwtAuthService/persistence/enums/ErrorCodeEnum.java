// src/main/java/com/apirest/JwtAuthService/persistence/enums/ErrorCodeEnum.java
package com.apirest.JwtAuthService.persistence.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCodeEnum {
    // Errores de autenticación
    ACCESS_DENIED(1001, "Acceso denegado: no tienes los permisos necesarios", HttpStatus.FORBIDDEN),
    INVALID_TOKEN(1002, "Token inválido", HttpStatus.UNAUTHORIZED),
    EXPIRED_TOKEN(1003, "Token JWT expirado", HttpStatus.UNAUTHORIZED),
    BAD_CREDENTIALS(1004, "Usuario o contraseña incorrectos", HttpStatus.UNAUTHORIZED),
    INSUFFICIENT_PERMISSIONS(1005, "No tienes los permisos requeridos para esta acción", HttpStatus.FORBIDDEN),
    AUTHENTICATION_FAILED(1006, "Falló la autenticación", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1007, "No autenticado", HttpStatus.UNAUTHORIZED),
    TOKEN_MISSING(1008, "Token no proporcionado en la cabecera Authorization", HttpStatus.UNAUTHORIZED),


    // Errores de validación
    INVALID_REQUEST(2001, "Solicitud inválida", HttpStatus.BAD_REQUEST),
    MISSING_REQUIRED_FIELDS(2002, "Campos requeridos faltantes", HttpStatus.BAD_REQUEST),

    // Errores de servicio
    USER_NOT_FOUND(3000, "Usuario no encontrado", HttpStatus.NOT_FOUND),
    USER_ALREADY_EXISTS(3001, "El usuario ya existe", HttpStatus.CONFLICT),

    RESOURCE_NOT_FOUND(3010, "Recurso no encontrado", HttpStatus.NOT_FOUND),
    RESOURCE_ALREADY_EXISTS(3011, "El recurso ya existe", HttpStatus.CONFLICT),

    PERMISSION_NOT_FOUND(3020, "Permiso no encontrado", HttpStatus.NOT_FOUND),
    PERMISSION_ALREADY_EXISTS(3021, "El permiso ya existe", HttpStatus.CONFLICT),
    PERMISSIONS_NOT_FOUND(3022, "Permisos no encontrados", HttpStatus.NOT_FOUND),

    ROLE_NOT_FOUND(3030, "Rol no encontrado", HttpStatus.NOT_FOUND),
    ROLE_ALREADY_EXISTS(3031, "El rol ya existe", HttpStatus.CONFLICT),
    INVALID_ROLES(3032, "Roles especificados no válidos", HttpStatus.BAD_REQUEST),

    // Errores internos
    INTERNAL_SERVER_ERROR(5000, "Error interno del servidor", HttpStatus.INTERNAL_SERVER_ERROR);

    private final int code;
    private final String message;
    private final HttpStatus httpStatus;
}