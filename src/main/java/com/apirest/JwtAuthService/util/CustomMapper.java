package com.apirest.JwtAuthService.util;

import com.apirest.JwtAuthService.auth.JwtUtils;
import com.apirest.JwtAuthService.controller.dtos.permission.PermissionCreateRequest;
import com.apirest.JwtAuthService.controller.dtos.permission.PermissionResponse;
import com.apirest.JwtAuthService.controller.dtos.permission.PermissionUpdateRequest;
import com.apirest.JwtAuthService.controller.dtos.permission.ResourcePermissionResponse;
import com.apirest.JwtAuthService.controller.dtos.resource.ResourceCreateRequest;
import com.apirest.JwtAuthService.controller.dtos.resource.ResourceResponse;
import com.apirest.JwtAuthService.controller.dtos.resource.ResourceUpdateRequest;
import com.apirest.JwtAuthService.controller.dtos.response.PageResponse;
import com.apirest.JwtAuthService.controller.dtos.role.PermissionsRoleResponse;
import com.apirest.JwtAuthService.controller.dtos.role.RoleCreateRequest;
import com.apirest.JwtAuthService.controller.dtos.role.RoleResponse;
import com.apirest.JwtAuthService.controller.dtos.role.RoleUpdateRequest;
import com.apirest.JwtAuthService.controller.dtos.user.*;
import com.apirest.JwtAuthService.persistence.entity.Permission;
import com.apirest.JwtAuthService.persistence.entity.Resource;
import com.apirest.JwtAuthService.persistence.entity.Role;
import com.apirest.JwtAuthService.persistence.entity.User;
import com.apirest.JwtAuthService.persistence.enums.Status;
import com.apirest.JwtAuthService.services.exception.MissingTokenException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CustomMapper {

    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;

    public <T> PageResponse<T> toPageResponse(Page<T> page){
        return new PageResponse<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isFirst(),
                page.isLast()
        );
    }

    //User
    public UserReponse entityUserToDto(User user){
        return new UserReponse(
                user.getUserId(),
                user.getUsername(),
                user.isEnabled(),
                user.isAccountNoExpired(),
                user.isAccountNonLocked(),
                user.isCredentialsNonExpired(),
                user.getRoles()
                        .stream()
                        .map(role -> new RoleUserReponse(
                                role.getRoleId(),
                                role.getRole(),
                                role.getPermissions()
                                        .stream()
                                        .map(permission -> new PermissionRoleUserResponse(
                                                permission.getPermissionId(),
                                                permission.getName(),
                                                permission.getDescription()
                                        )).toList()
                        )).toList()
        );
    }

    public User dtoToEntityUser(UserCreateRequest userCreateRequest, List<Role> roles, HttpServletRequest request){
        return User.builder()
                .username(userCreateRequest.username())
                .password(passwordEncoder.encode(userCreateRequest.password()))
                .roles(roles
                        .stream()
                        .filter(Objects::nonNull)
                        .collect(Collectors.toSet()))
                .isEnabled(true)
                .accountNoExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .usuReg(getUserFromRequest(request))
                .createdAt(LocalDateTime.now())
                .status(Status.ACTIVO)
                .build();
    }

    public User dtoToUpdateEntityUser(User user, UserUpdateRequest userUpdateRequest, HttpServletRequest request){
        return null;
    }

    //Role
    public RoleResponse entityRoleToDto(Role role){
        return new RoleResponse(
                role.getRoleId(),
                role.getRole(),
                role.getDescription(),
                role.getPermissions().stream()
                    .map(permission -> new PermissionsRoleResponse(
                            permission.getPermissionId(),
                            permission.getName(),
                            permission.getDescription()
                    )).toList(),
                role.getUsuReg(),
                role.getUsuMod(),
                role.getCreatedAt(),
                role.getUpdatedAt()
        );
    }

    public Role dtoToEntityRole (RoleCreateRequest roleCreateRequest, List<Permission> permissions, HttpServletRequest request){
        return Role.builder()
                .role(roleCreateRequest.role())
                .description(roleCreateRequest.description())
                .permissions(
                        permissions.stream()
                                .filter(Objects::nonNull)
                                .collect(Collectors.toSet()))
                .status(Status.ACTIVO)
                .usuReg(getUserFromRequest(request))
                .createdAt(LocalDateTime.now())
                .build();
    }

    public Role dtoToUpdateEntityRole (Role role, RoleUpdateRequest roleUpdateRequest, HttpServletRequest request){
        if (roleUpdateRequest.role() != null && !role.getRole().equals(roleUpdateRequest.role())) {
            role.setRole(roleUpdateRequest.role());
        }
        if (roleUpdateRequest.description() != null && !role.getDescription().equals(roleUpdateRequest.description())) {
            role.setDescription(roleUpdateRequest.description());
        }

        role.setUsuMod(getUserFromRequest(request));
        role.setUpdatedAt(LocalDateTime.now());

        return role;
    }

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
                resource.getUpdatedAt(),
                resource.getStatus()
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
                permission.getUpdatedAt(),
                permission.getStatus()
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
