package com.apirest.JwtAuthService.controller.advice;

import com.apirest.JwtAuthService.persistence.enums.ErrorCodeEnum;
import com.apirest.JwtAuthService.services.exception.ApiException;
import com.apirest.JwtAuthService.services.exception.InsufficientPermissionsException;
import com.apirest.JwtAuthService.util.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorResponse> handleApiException(ApiException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                ex.getErrorCode().name(),
                ex.getMessage(),
                ex.getErrorCode().getCode()
        );

        HttpStatus status = determineHttpStatus(ex.getErrorCode());

        return new ResponseEntity<>(errorResponse, status);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {

        ErrorResponse errorResponse = new ErrorResponse(
                ErrorCodeEnum.INTERNAL_SERVER_ERROR.name(),
                "Ocurrió un error inesperado",
                ErrorCodeEnum.INTERNAL_SERVER_ERROR.getCode()
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({
            AuthorizationDeniedException.class,
            AccessDeniedException.class
    })
    public ResponseEntity<ErrorResponse> handleAuthorizationDenied(Exception ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                ErrorCodeEnum.ACCESS_DENIED.name(),
                ErrorCodeEnum.ACCESS_DENIED.getMessage(),
                ErrorCodeEnum.ACCESS_DENIED.getCode()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(InsufficientPermissionsException.class)
    public ResponseEntity<ErrorResponse> handleInsufficientPermissions(InsufficientPermissionsException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                ex.getErrorCode().name(),
                ex.getMessage(),
                ex.getErrorCode().getCode()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }

    private HttpStatus determineHttpStatus(ErrorCodeEnum errorCode) {
        return switch (errorCode) {
            case ACCESS_DENIED -> HttpStatus.FORBIDDEN;
            case INVALID_TOKEN, EXPIRED_TOKEN, BAD_CREDENTIALS, TOKEN_MISSING -> HttpStatus.UNAUTHORIZED;
            case USER_NOT_FOUND, INVALID_ROLES, RESOURCE_NOT_FOUND,PERMISSION_NOT_FOUND -> HttpStatus.NOT_FOUND;
            case USER_ALREADY_EXISTS,RESOURCE_ALREADY_EXISTS,PERMISSION_ALREADY_EXISTS -> HttpStatus.CONFLICT;
            case INVALID_REQUEST, MISSING_REQUIRED_FIELDS -> HttpStatus.BAD_REQUEST;
            default -> HttpStatus.INTERNAL_SERVER_ERROR;
        };
    }
}
