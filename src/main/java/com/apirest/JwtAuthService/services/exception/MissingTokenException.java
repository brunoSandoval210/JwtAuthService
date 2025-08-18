package com.apirest.JwtAuthService.services.exception;

import com.apirest.JwtAuthService.persistence.enums.ErrorCodeEnum;

public class MissingTokenException extends ApiException {
    public MissingTokenException() {
        super(ErrorCodeEnum.TOKEN_MISSING, ErrorCodeEnum.TOKEN_MISSING.getMessage());
    }
}
