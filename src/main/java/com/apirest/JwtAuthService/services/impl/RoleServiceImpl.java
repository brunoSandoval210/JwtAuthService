package com.apirest.JwtAuthService.services.impl;

import com.apirest.JwtAuthService.controller.dtos.role.PermissionsRoleCreate;
import com.apirest.JwtAuthService.controller.dtos.role.RoleCreateRequest;
import com.apirest.JwtAuthService.controller.dtos.role.RoleResponse;
import com.apirest.JwtAuthService.controller.dtos.role.RoleUpdateRequest;
import com.apirest.JwtAuthService.persistence.entity.Permission;
import com.apirest.JwtAuthService.persistence.entity.Role;
import com.apirest.JwtAuthService.persistence.enums.ErrorCodeEnum;
import com.apirest.JwtAuthService.persistence.enums.Status;
import com.apirest.JwtAuthService.persistence.repository.PermissionRepository;
import com.apirest.JwtAuthService.persistence.repository.RoleRepository;
import com.apirest.JwtAuthService.services.exception.PermissionException;
import com.apirest.JwtAuthService.services.exception.RoleException;
import com.apirest.JwtAuthService.services.interfaces.RoleService;
import com.apirest.JwtAuthService.util.CustomMapper;
import com.apirest.JwtAuthService.util.PageResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final CustomMapper mapper;
    private final PermissionRepository permissionRepository;


    @Transactional(readOnly = true)
    @Override
    public PageResponse<RoleResponse> getAll(Pageable pageable) {
        return mapper.toPageResponse(roleRepository.findAll(pageable)
                .map(mapper::entityRoleToDto));
    }

    @Override
    public RoleResponse getBy(String s) {
        return null;
    }

    @Transactional(readOnly = true)
    @Override
    public RoleResponse getById(Long id) {
        return roleRepository.findById(id)
                .map(mapper::entityRoleToDto)
                .orElseThrow(() -> new RoleException(ErrorCodeEnum.ROLE_NOT_FOUND));
    }

    @Transactional
    @Override
    public RoleResponse save(RoleCreateRequest roleCreateRequest, HttpServletRequest request) {
        if (roleRepository.findByRole(roleCreateRequest.role()).isPresent()) {
            throw new RoleException(ErrorCodeEnum.ROLE_ALREADY_EXISTS);
        }

        List<Permission> permissions = permissionRepository.findByPermissionIdIn(roleCreateRequest.permissions()
                .stream()
                .map(PermissionsRoleCreate::permissionId)
                .filter(Objects::nonNull)
                .collect(Collectors.toList()));

        if (permissions.isEmpty()) {
            throw new PermissionException(ErrorCodeEnum.PERMISSIONS_NOT_FOUND);
        }

        Role role = mapper.dtoToEntityRole(roleCreateRequest,permissions,request);

        return mapper.entityRoleToDto(roleRepository.save(role));
    }

    @Transactional
    @Override
    public RoleResponse update(Long aLong, RoleUpdateRequest roleUpdateRequest, HttpServletRequest request) {
        Role role = roleRepository.findById(aLong)
                .orElseThrow(() -> new RoleException(ErrorCodeEnum.ROLE_NOT_FOUND));

        role = mapper.dtoToUpdateEntityRole(role, roleUpdateRequest, request);

        //Actualizar permisos
        Set<Long> currentPermissionIds = role.getPermissions().stream()
                .map(Permission::getPermissionId)
                .collect(Collectors.toSet());

        // permisos solicitados
        Set<Long> requestedPermissionIds = roleUpdateRequest.permissions().stream()
                .map(PermissionsRoleCreate::permissionId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        // calcular diferencias
        Set<Long> toAdd = new HashSet<>(requestedPermissionIds);
        toAdd.removeAll(currentPermissionIds);

        Set<Long> toRemove = new HashSet<>(currentPermissionIds);
        toRemove.removeAll(requestedPermissionIds);

        if (!toAdd.isEmpty()) {
            List<Permission> permissionsToAdd = permissionRepository.findByPermissionIdIn(new ArrayList<>(toAdd));
            role.getPermissions().addAll(permissionsToAdd);
        }

        if (!toRemove.isEmpty()) {
            role.getPermissions().removeIf(p -> toRemove.contains(p.getPermissionId()));
        }

        return mapper.entityRoleToDto(roleRepository.save(role));
    }

    @Transactional
    @Override
    public RoleResponse delete(Long aLong, HttpServletRequest request) {
        Role role = roleRepository.findById(aLong).orElseThrow(
                () -> new RoleException(ErrorCodeEnum.ROLE_NOT_FOUND));

        role.setStatus(role.getStatus() == Status.ACTIVO ? Status.INACTIVO : Status.ACTIVO);
        role.setUsuMod(mapper.getUserFromRequest(request));
        role.setUpdatedAt(LocalDateTime.now());

        return mapper.entityRoleToDto(roleRepository.save(role));
    }
}
