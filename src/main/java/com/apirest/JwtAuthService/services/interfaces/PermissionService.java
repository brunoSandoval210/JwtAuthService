package com.apirest.JwtAuthService.services.interfaces;

import com.apirest.JwtAuthService.controller.dtos.Permission.PermissionCreateRequest;
import com.apirest.JwtAuthService.controller.dtos.Permission.PermissionResponse;
import com.apirest.JwtAuthService.controller.dtos.Permission.PermissionUpdateRequest;
import com.apirest.JwtAuthService.util.CrudService;
import jakarta.servlet.http.HttpServletRequest;

public interface PermissionService extends CrudService<PermissionResponse,String, PermissionCreateRequest, HttpServletRequest,Long, PermissionUpdateRequest> {
}
