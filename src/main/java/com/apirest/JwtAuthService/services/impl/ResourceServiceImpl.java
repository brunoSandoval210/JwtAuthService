package com.apirest.JwtAuthService.services.impl;

import com.apirest.JwtAuthService.controller.dtos.resource.ResourceCreateRequest;
import com.apirest.JwtAuthService.controller.dtos.resource.ResourceResponse;
import com.apirest.JwtAuthService.controller.dtos.resource.ResourceUpdateRequest;
import com.apirest.JwtAuthService.persistence.entity.Resource;
import com.apirest.JwtAuthService.persistence.enums.ErrorCodeEnum;
import com.apirest.JwtAuthService.persistence.enums.Status;
import com.apirest.JwtAuthService.persistence.repository.ResourceRepository;
import com.apirest.JwtAuthService.services.exception.ResourceException;
import com.apirest.JwtAuthService.services.interfaces.ResourceService;
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
public class ResourceServiceImpl implements ResourceService {

    private final ResourceRepository resourceRepository;
    private final CustomMapper mapper;

    @Transactional(readOnly = true)
    @Override
    public List<ResourceResponse> getAll() {
        List<Resource> resources = resourceRepository.findAll();
        return resources.stream().map(mapper::entityResourceToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public ResourceResponse getBy(String name) {
        Resource resource = resourceRepository.findByName(name).orElseThrow(
                () -> new ResourceException(ErrorCodeEnum.RESOURCE_NOT_FOUND)
        );
        return mapper.entityResourceToDto(resource);
    }

    @Transactional
    @Override
    public ResourceResponse save(ResourceCreateRequest resourceCreateRequest, HttpServletRequest request) {
        if (resourceRepository.existsByName(resourceCreateRequest.name())){
            throw new ResourceException(ErrorCodeEnum.RESOURCE_ALREADY_EXISTS);
        }
        Resource resource = mapper.dtoToEntityResource(resourceCreateRequest,request);
        return mapper.entityResourceToDto(resourceRepository.save(resource));
    }

    @Transactional
    @Override
    public ResourceResponse update(Long aLong, ResourceUpdateRequest resourceUpdateRequest, HttpServletRequest request) {
        Resource resource = resourceRepository.findById(aLong).orElseThrow(
                ()-> new ResourceException(ErrorCodeEnum.RESOURCE_NOT_FOUND));
        resource = mapper.dtoToUpdateEntityResource(resource, resourceUpdateRequest, request);

        return mapper.entityResourceToDto(resourceRepository.save(resource));
    }

    @Transactional
    @Override
    public ResourceResponse delete(Long aLong, HttpServletRequest request) {
        Resource resource = resourceRepository.findById(aLong).orElseThrow(
                ()-> new ResourceException(ErrorCodeEnum.RESOURCE_NOT_FOUND));

        resource.setStatus(resource.getStatus() == Status.ACTIVO ? Status.INACTIVO: Status.ACTIVO);
        resource.setUsuMod(mapper.getUserFromRequest(request));
        resource.setUpdatedAt(LocalDateTime.now());

        return mapper.entityResourceToDto(resourceRepository.save(resource));
    }
}
