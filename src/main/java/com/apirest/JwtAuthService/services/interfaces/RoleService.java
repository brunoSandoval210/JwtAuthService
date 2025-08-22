package com.apirest.JwtAuthService.services.interfaces;

import com.apirest.JwtAuthService.controller.dtos.role.RoleCreateRequest;
import com.apirest.JwtAuthService.controller.dtos.role.RoleResponse;
import com.apirest.JwtAuthService.controller.dtos.role.RoleUpdateRequest;
import com.apirest.JwtAuthService.util.CrudService;
import jakarta.servlet.http.HttpServletRequest;

public interface RoleService extends CrudService<RoleResponse, String, RoleCreateRequest, HttpServletRequest,Long, RoleUpdateRequest> {
}
