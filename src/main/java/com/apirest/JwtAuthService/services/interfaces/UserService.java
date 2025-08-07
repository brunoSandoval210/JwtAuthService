package com.apirest.JwtAuthService.services.interfaces;

import com.apirest.JwtAuthService.controller.dtos.user.UserCreateRequest;
import com.apirest.JwtAuthService.controller.dtos.user.UserReponse;

import java.util.List;

public interface UserService {
    List<UserReponse> getAllUsers();
    UserReponse getFindByUsername(String username);
    UserReponse saveUser(UserCreateRequest userCreateRequest);
    String deleteUser(String username);
}
