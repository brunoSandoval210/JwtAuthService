package com.apirest.JwtAuthService.services;

import com.apirest.JwtAuthService.persistence.PermissionRepository;
import com.apirest.JwtAuthService.persistence.RoleRepository;
import com.apirest.JwtAuthService.services.impl.RoleServiceImpl;
import com.apirest.JwtAuthService.util.CustomMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class RoleServiceTest {

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private CustomMapper mapper;

    @Mock
    private PermissionRepository permissionRepository;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private RoleServiceImpl roleService;

}
