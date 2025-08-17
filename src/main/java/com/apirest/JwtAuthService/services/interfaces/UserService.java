package com.apirest.JwtAuthService.services.interfaces;

import com.apirest.JwtAuthService.controller.dtos.user.UserCreateRequest;
import com.apirest.JwtAuthService.controller.dtos.user.UserReponse;
import com.apirest.JwtAuthService.controller.dtos.user.UserUpdateRequest;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface UserService {
    List<UserReponse> getAllUsers();
    UserReponse getFindByUsername(String username);
    UserReponse saveUser(UserCreateRequest userCreateRequest, HttpServletRequest request);
    UserReponse updateUser(Long userId,UserUpdateRequest userUpdateRequest, HttpServletRequest request);
    String deleteUser(String username);
}
