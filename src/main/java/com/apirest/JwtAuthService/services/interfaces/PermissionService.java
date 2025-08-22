package com.apirest.JwtAuthService.services.interfaces;

import com.apirest.JwtAuthService.controller.dtos.permission.PermissionCreateRequest;
import com.apirest.JwtAuthService.controller.dtos.permission.PermissionResponse;
import com.apirest.JwtAuthService.controller.dtos.permission.PermissionUpdateRequest;
import com.apirest.JwtAuthService.util.CrudService;
import jakarta.servlet.http.HttpServletRequest;

public interface PermissionService extends CrudService<PermissionResponse,String, PermissionCreateRequest, HttpServletRequest,Long, PermissionUpdateRequest> {
}
