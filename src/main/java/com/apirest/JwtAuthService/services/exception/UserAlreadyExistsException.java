package com.apirest.JwtAuthService.services.exception;

import com.apirest.JwtAuthService.persistence.enums.ErrorCodeEnum;

public class UserAlreadyExistsException extends ApiException {
    public UserAlreadyExistsException(String username) {
        super(ErrorCodeEnum.USER_ALREADY_EXISTS, "El usuario " + username + " ya existe");
    }
}