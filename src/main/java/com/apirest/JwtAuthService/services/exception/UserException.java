package com.apirest.JwtAuthService.services.exception;

import com.apirest.JwtAuthService.persistence.enums.ErrorCodeEnum;

public class UserException extends ApiException{
    public UserException(ErrorCodeEnum errorCode) {
        super(errorCode, errorCode.getMessage());
    }

    public UserException(ErrorCodeEnum errorCode, String customMessage) {
        super(errorCode, customMessage);
    }
}
