package com.apirest.JwtAuthService.services.impl;

import com.apirest.JwtAuthService.controller.dtos.user.*;
import com.apirest.JwtAuthService.persistence.entity.Role;
import com.apirest.JwtAuthService.persistence.entity.User;
import com.apirest.JwtAuthService.persistence.enums.ErrorCodeEnum;
import com.apirest.JwtAuthService.persistence.enums.Status;
import com.apirest.JwtAuthService.persistence.repository.RoleRepository;
import com.apirest.JwtAuthService.persistence.repository.UserRepository;
import com.apirest.JwtAuthService.services.exception.RoleException;
import com.apirest.JwtAuthService.services.exception.UserException;
import com.apirest.JwtAuthService.services.interfaces.UserService;
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
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final CustomMapper mapper;

    @Transactional(readOnly = true)
    @Override
    public PageResponse<UserReponse> getAll(Pageable pageable) {
        return mapper.toPageResponse(userRepository.findAll(pageable)
                .map(mapper::entityUserToDto));
    }

    @Override
    public UserReponse getBy(String s) {
        return null;
    }

    @Transactional(readOnly = true)
    @Override
    public UserReponse getById(Long id) {
        return mapper.entityUserToDto(userRepository.findById(id)
                .orElseThrow(() -> new UserException(ErrorCodeEnum.USER_NOT_FOUND)));
    }

    @Transactional
    @Override
    public UserReponse save(UserCreateRequest userCreateRequest, HttpServletRequest request) {
        if (userRepository.existsByUsername(userCreateRequest.username())){
            throw new UserException(ErrorCodeEnum.USER_ALREADY_EXISTS);
        }

        List<Role> roles = roleRepository.findByRoleIdIn(userCreateRequest.roleCreateRequest().roleIds());

        if (roles.isEmpty()){
            throw new RoleException(ErrorCodeEnum.INVALID_ROLES);
        }

        User user = mapper.dtoToEntityUser(userCreateRequest,roles,request);

        return mapper.entityUserToDto(userRepository.save(user));
    }

    @Transactional
    @Override
    public UserReponse update(Long aLong, UserUpdateRequest userUpdateRequest, HttpServletRequest request) {
        User user = userRepository.findById(aLong)
                .orElseThrow(() -> new UserException(ErrorCodeEnum.USER_NOT_FOUND));

        user = mapper.dtoToUpdateEntityUser(user, userUpdateRequest, request);

        Set<Long> currentRoleIds = user.getRoles().stream()
                .map(Role::getRoleId)
                .collect(Collectors.toSet());

        Set<Long> requestedRoleIds = userUpdateRequest.roleCreateRequest().roleIds()
                .stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        Set<Long> toAdd = new HashSet<>(requestedRoleIds);
        toAdd.removeAll(currentRoleIds);

        Set<Long> toRemove = new HashSet<>(currentRoleIds);
        toRemove.removeAll(requestedRoleIds);

        if (!toAdd.isEmpty()){
            List<Role> rolesToAdd = roleRepository.findByRoleIdIn(new ArrayList<>(toAdd));
            user.getRoles().addAll(rolesToAdd);
        }

        if (!toRemove.isEmpty()){
            user.getRoles().removeIf( r -> toRemove.contains(r.getRoleId()));
        }

        return mapper.entityUserToDto(userRepository.save(user));
    }

    @Transactional
    @Override
    public UserReponse delete(Long aLong, HttpServletRequest request) {
        User user = userRepository.findById(aLong)
                .orElseThrow(() -> new UserException(ErrorCodeEnum.USER_NOT_FOUND));

        user.setStatus(user.getStatus() == Status.ACTIVO ? Status.INACTIVO : Status.ACTIVO);
        user.setUsuMod(mapper.getUserFromRequest(request));
        user.setUpdatedAt(LocalDateTime.now());

        return mapper.entityUserToDto(userRepository.save(user));
    }

}
