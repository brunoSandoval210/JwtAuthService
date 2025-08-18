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
    USER_NOT_FOUND(1005, "Usuario no encontrado", HttpStatus.NOT_FOUND),
    USER_ALREADY_EXISTS(1006, "El usuario ya existe", HttpStatus.CONFLICT),
    INSUFFICIENT_PERMISSIONS(1007, "No tienes los permisos requeridos para esta acción", HttpStatus.FORBIDDEN),
    AUTHENTICATION_FAILED(1008, "Falló la autenticación", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1009, "No autenticado", HttpStatus.UNAUTHORIZED),
    TOKEN_MISSING(1010, "Token no proporcionado en la cabecera Authorization", HttpStatus.UNAUTHORIZED),


    // Errores de validación
    INVALID_REQUEST(2001, "Solicitud inválida", HttpStatus.BAD_REQUEST),
    MISSING_REQUIRED_FIELDS(2002, "Campos requeridos faltantes", HttpStatus.BAD_REQUEST),
    INVALID_ROLES(2003, "Roles especificados no válidos", HttpStatus.BAD_REQUEST),

    // Errores de servicio
    USER_DELETION_FAILED(3001, "Error al eliminar usuario", HttpStatus.INTERNAL_SERVER_ERROR),
    USER_CREATION_FAILED(3002, "Error al crear usuario", HttpStatus.INTERNAL_SERVER_ERROR),

    RESOURCE_NOT_FOUND(3003, "Recurso no encontrado", HttpStatus.NOT_FOUND),
    RESOURCE_ALREADY_EXISTS(3004, "El recurso ya existe", HttpStatus.CONFLICT),

    PERMISSION_NOT_FOUND(3010, "Permiso no encontrado", HttpStatus.NOT_FOUND),
    PERMISSION_ALREADY_EXISTS(3011, "El permiso ya existe", HttpStatus.CONFLICT),


    // Errores internos
    INTERNAL_SERVER_ERROR(5000, "Error interno del servidor", HttpStatus.INTERNAL_SERVER_ERROR);

    private final int code;
    private final String message;
    private final HttpStatus httpStatus;
}