package com.apirest.JwtAuthService.services.impl;

import com.apirest.JwtAuthService.controller.dtos.permission.PermissionCreateRequest;
import com.apirest.JwtAuthService.controller.dtos.permission.PermissionResponse;
import com.apirest.JwtAuthService.controller.dtos.permission.PermissionUpdateRequest;
import com.apirest.JwtAuthService.persistence.entity.Permission;
import com.apirest.JwtAuthService.persistence.entity.Resource;
import com.apirest.JwtAuthService.persistence.enums.ErrorCodeEnum;
import com.apirest.JwtAuthService.persistence.enums.Status;
import com.apirest.JwtAuthService.persistence.PermissionRepository;
import com.apirest.JwtAuthService.persistence.ResourceRepository;
import com.apirest.JwtAuthService.services.exception.PermissionException;
import com.apirest.JwtAuthService.services.exception.ResourceException;
import com.apirest.JwtAuthService.services.interfaces.PermissionService;
import com.apirest.JwtAuthService.util.CustomMapper;
import com.apirest.JwtAuthService.controller.dtos.response.PageResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {

    private final PermissionRepository permissionRepository;
    private final ResourceRepository resourceRepository;
    private final CustomMapper mapper;

    @Transactional(readOnly = true)
    @Override
    public PageResponse<PermissionResponse> getAll(Pageable pageable) {
        return mapper.toPageResponse(permissionRepository.findAll(pageable)
                .map(mapper::entityPermissionToDto));
    }

    @Override
    public PermissionResponse getBy(String s) {
        return null;
    }

    @Transactional
    @Override
    public PermissionResponse save(PermissionCreateRequest permissionCreateRequest, HttpServletRequest request) {
        Resource resource = null;
        if (permissionRepository.existsByName(permissionCreateRequest.name())){
            throw new PermissionException(ErrorCodeEnum.PERMISSION_ALREADY_EXISTS);
        }
        if (permissionCreateRequest.resourceId() != null){
             resource = resourceRepository.findById(permissionCreateRequest.resourceId()).orElseThrow(
                    ()-> new ResourceException(ErrorCodeEnum.RESOURCE_NOT_FOUND));
        }

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
            resource = resourceRepository.findById(permissionUpdateRequest.resourceId()).orElseThrow(
                    () -> new ResourceException(ErrorCodeEnum.RESOURCE_NOT_FOUND)
            );
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

    @Transactional(readOnly = true)
    @Override
    public PermissionResponse getById(Long id) {
        return mapper.entityPermissionToDto(permissionRepository.findById(id).orElseThrow(
                () -> new PermissionException(ErrorCodeEnum.PERMISSION_NOT_FOUND)));
    }
}
