package com.apirest.JwtAuthService.util;

import com.apirest.JwtAuthService.controller.dtos.Permission.PermissionCreateRequest;
import com.apirest.JwtAuthService.controller.dtos.Permission.PermissionResponse;
import com.apirest.JwtAuthService.controller.dtos.Permission.PermissionUpdateRequest;
import com.apirest.JwtAuthService.controller.dtos.Permission.ResourcePermissionResponse;
import com.apirest.JwtAuthService.controller.dtos.resource.ResourceCreateRequest;
import com.apirest.JwtAuthService.controller.dtos.resource.ResourceResponse;
import com.apirest.JwtAuthService.controller.dtos.resource.ResourceUpdateRequest;
import com.apirest.JwtAuthService.controller.dtos.user.PermissionRoleUserResponse;
import com.apirest.JwtAuthService.persistence.entity.Permission;
import com.apirest.JwtAuthService.persistence.entity.Resource;
import com.apirest.JwtAuthService.persistence.enums.Status;
import com.apirest.JwtAuthService.services.exception.MissingTokenException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class CustomMapper {

    private final JwtUtils jwtUtils;

    //Resource
    public ResourceResponse entityResourceToDto(Resource resource) {
        return new ResourceResponse(
                resource.getResourceId(),
                resource.getName(),
                resource.getDescription(),
                resource.getPermissions().stream()
                        .map(permission -> new PermissionRoleUserResponse(
                                permission.getPermissionId(),
                                permission.getName(),
                                permission.getDescription()))
                        .toList(),
                resource.getUsuReg(),
                resource.getUsuMod(),
                resource.getCreatedAt(),
                resource.getUpdatedAt()
        );
    }

    public Resource dtoToEntityResource (ResourceCreateRequest resourceCreateRequest, HttpServletRequest request){
        return Resource.builder()
                .name(resourceCreateRequest.name())
                .description(resourceCreateRequest.description())
                .status(Status.ACTIVO)
                .usuReg(getUserFromRequest(request))
                .createdAt(LocalDateTime.now())
                .build();
    }

    public Resource dtoToUpdateEntityResource (Resource resource, ResourceUpdateRequest resourceUpdateRequest, HttpServletRequest request){
        if (resourceUpdateRequest.name() != null && !resource.getName().equals(resourceUpdateRequest.name())) {
            resource.setName(resourceUpdateRequest.name());
        }
        if (resourceUpdateRequest.description() != null && !resourceUpdateRequest.description().equals(resource.getDescription())) {
            resource.setDescription(resourceUpdateRequest.description());
        }

        resource.setUsuMod(getUserFromRequest(request));
        resource.setUpdatedAt(LocalDateTime.now());

        return resource;
    }

    //Permission

    public PermissionResponse entityPermissionToDto(Permission permission){
        return new PermissionResponse(
                permission.getPermissionId(),
                permission.getName(),
                permission.getDescription(),
                Optional.ofNullable(permission.getResource())
                        .map(resource -> new ResourcePermissionResponse(
                                resource.getResourceId(),
                                resource.getName(),
                                resource.getDescription()))
                        .orElse(null),
                permission.getUsuReg(),
                permission.getUsuMod(),
                permission.getCreatedAt(),
                permission.getUpdatedAt()
        );
    }

    public Permission dtoToEntityPermission (PermissionCreateRequest permissionCreateRequest, Resource resource,HttpServletRequest request){
        return Permission.builder()
                .name(permissionCreateRequest.name())
                .description(permissionCreateRequest.description())
                .resource(resource)
                .status(Status.ACTIVO)
                .usuReg(getUserFromRequest(request))
                .createdAt(LocalDateTime.now())
                .build();
    }

    public Permission dtoToUpdatePermission (Permission permission, PermissionUpdateRequest permissionUpdateRequest, Resource resource, HttpServletRequest request){
        if (permissionUpdateRequest.name() != null && !permissionUpdateRequest.name().equals(permission.getName())) {
            permission.setName(permissionUpdateRequest.name());
        }

        if (permissionUpdateRequest.description() != null && !permissionUpdateRequest.description().equals(permission.getDescription())) {
            permission.setDescription(permissionUpdateRequest.description());
        }

        if (resource != null) {
            permission.setResource(resource);
        }

        permission.setUsuMod(getUserFromRequest(request));
        permission.setUpdatedAt(LocalDateTime.now());

        return permission;
    }

    public String getUserFromRequest(HttpServletRequest request){
        if (request.getHeader(HttpHeaders.AUTHORIZATION) == null || !(request.getHeader(HttpHeaders.AUTHORIZATION)).startsWith("Bearer ")) {
            throw new MissingTokenException();
        }
        return Optional.ofNullable(request.getHeader(HttpHeaders.AUTHORIZATION))
                .map(header -> header.substring(7))
                .map(jwtUtils::validateToken)
                .map(jwtUtils::getUsernameFromToken)
                .orElseThrow(MissingTokenException::new);
    }

}
