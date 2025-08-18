package com.apirest.JwtAuthService.services.impl;

import com.apirest.JwtAuthService.controller.dtos.Permission.PermissionCreateRequest;
import com.apirest.JwtAuthService.controller.dtos.Permission.PermissionResponse;
import com.apirest.JwtAuthService.controller.dtos.Permission.PermissionUpdateRequest;
import com.apirest.JwtAuthService.persistence.entity.Permission;
import com.apirest.JwtAuthService.persistence.entity.Resource;
import com.apirest.JwtAuthService.persistence.enums.ErrorCodeEnum;
import com.apirest.JwtAuthService.persistence.enums.Status;
import com.apirest.JwtAuthService.persistence.repository.PermissionRepository;
import com.apirest.JwtAuthService.persistence.repository.ResourceRepository;
import com.apirest.JwtAuthService.services.exception.PermissionException;
import com.apirest.JwtAuthService.services.interfaces.PermissionService;
import com.apirest.JwtAuthService.util.CustomMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {

    private final PermissionRepository permissionRepository;
    private final ResourceRepository resourceRepository;
    private final CustomMapper mapper;

    @Transactional(readOnly = true)
    @Override
    public List<PermissionResponse> getAll() {
        return permissionRepository.findAll().stream().map(mapper::entityPermissionToDto).collect(Collectors.toList());
    }

    @Override
    public PermissionResponse getBy(String s) {
        return null;
    }

    @Transactional
    @Override
    public PermissionResponse save(PermissionCreateRequest permissionCreateRequest, HttpServletRequest request) {
        if (permissionRepository.existsByName(permissionCreateRequest.name())){
            throw new PermissionException(ErrorCodeEnum.PERMISSION_ALREADY_EXISTS);
        }

        Resource resource = resourceRepository.findById(permissionCreateRequest.resourceId()).orElseThrow(
                ()-> new PermissionException(ErrorCodeEnum.RESOURCE_NOT_FOUND));
        Permission permission= mapper.dtoToEntityPermission(permissionCreateRequest,resource,request);

        return mapper.entityPermissionToDto(permissionRepository.save(permission));
    }

    @Transactional
    @Override
    public PermissionResponse update(Long aLong, PermissionUpdateRequest permissionUpdateRequest, HttpServletRequest request) {
        Resource resource = null;
        Permission permission = permissionRepository.findById(aLong).orElseThrow(
                ()-> new PermissionException(ErrorCodeEnum.PERMISSION_NOT_FOUND));

        if (permissionUpdateRequest.resourceId() != null){
            resource = resourceRepository.findById(permissionUpdateRequest.resourceId()).orElse(null);
        }

        permission = mapper.dtoToUpdatePermission(permission, permissionUpdateRequest, resource, request);

        return mapper.entityPermissionToDto(permissionRepository.save(permission));
    }

    @Transactional
    @Override
    public PermissionResponse delete(Long aLong, HttpServletRequest request) {
        Permission permission = permissionRepository.findById(aLong).orElseThrow(
                () -> new PermissionException(ErrorCodeEnum.PERMISSION_NOT_FOUND));

        permission.setStatus(permission.getStatus() == Status.ACTIVO ? Status.INACTIVO : Status.ACTIVO);
        permission.setUsuMod(mapper.getUserFromRequest(request));
        permission.setUpdatedAt(LocalDateTime.now());

        return mapper.entityPermissionToDto(permissionRepository.save(permission));
    }
}
