package com.apirest.JwtAuthService.services.exception;

import com.apirest.JwtAuthService.persistence.enums.ErrorCodeEnum;

public class PermissionException extends ApiException {
    public PermissionException(ErrorCodeEnum errorCode) {
        super(errorCode, errorCode.getMessage());
    }

    public PermissionException(ErrorCodeEnum errorCode, String message) {
        super(errorCode, message);
    }
}
