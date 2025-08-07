package com.apirest.JwtAuthService.services.exception;

import com.apirest.JwtAuthService.persistence.enums.ErrorCodeEnum;

public class UserNotFoundException extends ApiException {
    public UserNotFoundException(String username) {
        super(ErrorCodeEnum.USER_NOT_FOUND, "Usuario " + username + " no encontrado");
    }
}