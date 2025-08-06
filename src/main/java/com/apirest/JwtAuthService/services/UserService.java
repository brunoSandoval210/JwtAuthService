package com.apirest.JwtAuthService.services;

import com.apirest.JwtAuthService.controller.dtos.UserReponse;

import java.util.List;

public interface UserService {
    List<UserReponse> getAllUsers();

}
