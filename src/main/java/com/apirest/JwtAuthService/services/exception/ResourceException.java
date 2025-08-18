package com.apirest.JwtAuthService.services.exception;

import com.apirest.JwtAuthService.persistence.enums.ErrorCodeEnum;

public class ResourceException extends ApiException {
    public ResourceException(ErrorCodeEnum errorCode) {
        super(errorCode, errorCode.getMessage());
    }

    public ResourceException(ErrorCodeEnum errorCode, String customMessage) {
        super(errorCode, customMessage);
    }
}
