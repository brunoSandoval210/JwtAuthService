package com.apirest.JwtAuthService.services.exception;

import com.apirest.JwtAuthService.persistence.enums.ErrorCodeEnum;

public class RoleException extends ApiException {
    public RoleException(ErrorCodeEnum errorCode) {
        super(errorCode, errorCode.getMessage());
    }

    public RoleException(ErrorCodeEnum errorCode, String customMessage) {
        super(errorCode, customMessage);
    }
}
