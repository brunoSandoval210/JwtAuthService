package com.apirest.JwtAuthService.services.impl;

import com.apirest.JwtAuthService.controller.dtos.user.*;
import com.apirest.JwtAuthService.persistence.entity.Role;
import com.apirest.JwtAuthService.persistence.entity.User;
import com.apirest.JwtAuthService.persistence.enums.ErrorCodeEnum;
import com.apirest.JwtAuthService.persistence.enums.Status;
import com.apirest.JwtAuthService.persistence.repository.RoleRepository;
import com.apirest.JwtAuthService.persistence.repository.UserRepository;
import com.apirest.JwtAuthService.services.exception.ApiException;
import com.apirest.JwtAuthService.services.exception.InvalidRolesException;
import com.apirest.JwtAuthService.services.exception.UserAlreadyExistsException;
import com.apirest.JwtAuthService.services.exception.UserNotFoundException;
import com.apirest.JwtAuthService.services.interfaces.UserService;
import com.apirest.JwtAuthService.util.JwtUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    @Override
    public List<UserReponse> getAllUsers() {
        List<UserReponse> userReponseList = new ArrayList<>();
        userRepository.findAll().forEach(user -> {
            UserReponse userReponse = buildUserResponse(user);
            userReponseList.add(userReponse);
        });
        return userReponseList;
    }

    @Transactional(readOnly = true)
    @Override
    public UserReponse getFindByUsername(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new UserNotFoundException(username));
        return buildUserResponse(user);
    }

    @Transactional
    @Override
    public UserReponse saveUser(UserCreateRequest userCreateRequest, HttpServletRequest request) {

        if (userRepository.existsByUsername(userCreateRequest.username())){
            throw new UserAlreadyExistsException(userCreateRequest.username());
        }

        List<String> roleRequest = userCreateRequest.roleCreateRequest().roleNames();
        Set<Role> roles = new HashSet<>(roleRepository.findByRoleIn(roleRequest));

        if (roles.isEmpty()){
            throw new InvalidRolesException();
        }

        Jws<Claims> validate = jwtUtils.validateToken((request.getHeader(HttpHeaders.AUTHORIZATION)).substring(7));
        String userCreate=jwtUtils.getUsernameFromToken(validate);

        User user = User.builder()
                .username(userCreateRequest.username())
                .password(passwordEncoder.encode(userCreateRequest.password()))
                .roles(roles)
                .isEnabled(true)
                .accountNoExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .usuReg(userCreate)
                .createdAt(LocalDateTime.now())
                .status(Status.ACTIVO)
                .build();

        user = userRepository.save(user);

        return buildUserResponse(user);
    }

    @Transactional
    @Override
    public UserReponse updateUser(Long userId, UserUpdateRequest userUpdateRequest, HttpServletRequest request) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException("Usuario con el id " + userId + " no encontrado"));

        Jws<Claims> validate = jwtUtils.validateToken((request.getHeader(HttpHeaders.AUTHORIZATION)).substring(7));
        String userUpdate = jwtUtils.getUsernameFromToken(validate);

        Set<Role> roles = new HashSet<>();
        if (userUpdateRequest.roleCreateRequest() != null && !userUpdateRequest.roleCreateRequest().roleNames().isEmpty()) {
            roles = new HashSet<>(roleRepository.findByRoleIn(userUpdateRequest.roleCreateRequest().roleNames()));
            if (roles.isEmpty()) {
                throw new InvalidRolesException();
            }
        }

        if (userUpdateRequest.password() != null) {
            user.setPassword(passwordEncoder.encode(userUpdateRequest.password()));
        }
        if (!roles.isEmpty()) {
            user.setRoles(roles);
        }

        user.setUpdatedAt(LocalDateTime.now());
        user.setUsuMod(userUpdate);

        user = userRepository.save(user);

        return buildUserResponse(user);
    }

    @Transactional
    @Override
    public String deleteUser(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new UserNotFoundException(username));
        userRepository.delete(user);

        if (userRepository.existsByUsername(username)) {
            throw new ApiException(ErrorCodeEnum.USER_DELETION_FAILED);
        }
        return "Usuario " + username + " eliminado correctamente";
    }

    // Método reutilizable para construir UserReponse
    private UserReponse buildUserResponse(User user) {
        return new UserReponse(
                user.getUsername(),
                user.isEnabled(),
                user.isAccountNoExpired(),
                user.isAccountNonLocked(),
                user.isCredentialsNonExpired(),
                user.getRoles().stream()
                        .map(role -> new RoleReponse(
                                role.getRole(),
                                role.getPermissions().stream()
                                        .map(permission -> new PermissionResponse(permission.getPermissionName()))
                                        .toList()
                        ))
                        .toList()
        );
    }
}
