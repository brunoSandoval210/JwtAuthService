package com.apirest.JwtAuthService.services;

import com.apirest.JwtAuthService.controller.dtos.PermissionResponse;
import com.apirest.JwtAuthService.controller.dtos.RoleReponse;
import com.apirest.JwtAuthService.controller.dtos.UserReponse;
import com.apirest.JwtAuthService.persistence.entity.User;
import com.apirest.JwtAuthService.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

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


}
