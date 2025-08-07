package com.apirest.JwtAuthService.services.impl;

import com.apirest.JwtAuthService.controller.dtos.user.PermissionResponse;
import com.apirest.JwtAuthService.controller.dtos.user.RoleReponse;
import com.apirest.JwtAuthService.controller.dtos.user.UserCreateRequest;
import com.apirest.JwtAuthService.controller.dtos.user.UserReponse;
import com.apirest.JwtAuthService.persistence.entity.Role;
import com.apirest.JwtAuthService.persistence.entity.User;
import com.apirest.JwtAuthService.persistence.enums.ErrorCodeEnum;
import com.apirest.JwtAuthService.persistence.repository.RoleRepository;
import com.apirest.JwtAuthService.persistence.repository.UserRepository;
import com.apirest.JwtAuthService.services.exception.ApiException;
import com.apirest.JwtAuthService.services.exception.InvalidRolesException;
import com.apirest.JwtAuthService.services.exception.UserAlreadyExistsException;
import com.apirest.JwtAuthService.services.exception.UserNotFoundException;
import com.apirest.JwtAuthService.services.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Transactional(readOnly = true)
    @Override
    public List<UserReponse> getAllUsers() {
        List<UserReponse> userReponseList = new ArrayList<>();
        userRepository.findAll().forEach(user -> {
            UserReponse userReponse = new UserReponse(
                    user.getUsername(),
                    user.isEnabled(),
                    user.isAccountNoExpired(),
                    user.isAccountNonLocked(),
                    user.isCredentialsNonExpired(),
                    user.getRoles().stream()
                            .map(role -> new RoleReponse(
                                    role.getRoleEnum().name(),
                                    role.getPermissions().stream()
                                            .map(permission -> new PermissionResponse(
                                                    permission.getPermissionName()
                                            ))
                                            .toList()
                            ))
                            .toList()
            );
            userReponseList.add(userReponse);
        });
        return userReponseList;
    }

    @Transactional(readOnly = true)
    @Override
    public UserReponse getFindByUsername(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new UserNotFoundException(username));
        return new UserReponse(
                user.getUsername(),
                user.isEnabled(),
                user.isAccountNoExpired(),
                user.isAccountNonLocked(),
                user.isCredentialsNonExpired(),
                user.getRoles().stream()
                        .map(role -> new RoleReponse(
                                role.getRoleEnum().name(),
                                role.getPermissions().stream()
                                        .map(permission -> new PermissionResponse(
                                                permission.getPermissionName()
                                        ))
                                        .toList()
                        ))
                        .toList()
        );
    }

    @Transactional
    @Override
    public UserReponse saveUser(UserCreateRequest userCreateRequest) {

        if (userRepository.existsByUsername(userCreateRequest.username())){
            throw new UserAlreadyExistsException(userCreateRequest.username());
        }

        List<String> roleRequest = userCreateRequest.roleCreateRequest().roleNames();
        Set<Role> roles = new HashSet<>(roleRepository.findByRoleEnumIn(roleRequest));

        if (roles.isEmpty()){
            throw new InvalidRolesException();
        }

        User user = User.builder()
                .username(userCreateRequest.username())
                .password(userCreateRequest.password())
                .roles(roles)
                .isEnabled(true)
                .accountNoExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .build();

        user = userRepository.save(user);

        return new UserReponse(
                user.getUsername(),
                user.isEnabled(),
                user.isAccountNoExpired(),
                user.isAccountNonLocked(),
                user.isCredentialsNonExpired(),
                user.getRoles().stream()
                        .map(role -> new RoleReponse(
                                role.getRoleEnum().name(),
                                role.getPermissions().stream()
                                        .map(permission -> new PermissionResponse(
                                                permission.getPermissionName()
                                        ))
                                        .toList()
                        ))
                        .toList()
        );
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
}
