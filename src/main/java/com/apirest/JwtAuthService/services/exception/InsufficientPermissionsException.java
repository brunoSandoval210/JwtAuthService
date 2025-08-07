package com.apirest.JwtAuthService.services.exception;

import com.apirest.JwtAuthService.persistence.enums.ErrorCodeEnum;

public class InsufficientPermissionsException extends ApiException{
    public InsufficientPermissionsException(String requiredPermission) {
        super(ErrorCodeEnum.INSUFFICIENT_PERMISSIONS,
                "Se requiere el permiso: " + requiredPermission);
    }
}
