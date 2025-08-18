package com.apirest.JwtAuthService.services.interfaces;

import com.apirest.JwtAuthService.controller.dtos.resource.ResourceCreateRequest;
import com.apirest.JwtAuthService.controller.dtos.resource.ResourceResponse;
import com.apirest.JwtAuthService.controller.dtos.resource.ResourceUpdateRequest;
import com.apirest.JwtAuthService.util.CrudService;
import jakarta.servlet.http.HttpServletRequest;

public interface ResourceService extends CrudService<ResourceResponse, String, ResourceCreateRequest, HttpServletRequest, Long,ResourceUpdateRequest> {

}
