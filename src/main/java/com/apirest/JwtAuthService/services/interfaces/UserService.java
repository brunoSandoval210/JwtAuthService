package com.apirest.JwtAuthService.services.interfaces;

import com.apirest.JwtAuthService.controller.dtos.user.UserCreateRequest;
import com.apirest.JwtAuthService.controller.dtos.user.UserReponse;
import com.apirest.JwtAuthService.controller.dtos.user.UserUpdateRequest;
import com.apirest.JwtAuthService.util.CrudService;
import jakarta.servlet.http.HttpServletRequest;


public interface UserService extends CrudService <UserReponse,String, UserCreateRequest,HttpServletRequest, Long, UserUpdateRequest>{

}
