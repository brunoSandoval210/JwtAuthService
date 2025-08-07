package com.apirest.JwtAuthService.services.exception;

import com.apirest.JwtAuthService.persistence.enums.ErrorCodeEnum;

public class InvalidRolesException extends ApiException {
    public InvalidRolesException() {
        super(ErrorCodeEnum.INVALID_ROLES, "Los roles especificados no son válidos");
    }
}