package com.apirest.JwtAuthService.util;

import com.apirest.JwtAuthService.auth.JwtUtils;
import com.apirest.JwtAuthService.controller.dtos.response.PageResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.util.List;

public class CustomMapperTest {

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private CustomMapper customMapper;

    @BeforeEach
    public  void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void toPageResponse_deberiaMapearCorrectamente() {
        // Arrange

        List<String> content = List.of("uno", "dos");
        Page<String> page = new PageImpl<>(content, PageRequest.of(0, 2), 2);

        // Act
        PageResponse<String> response = customMapper.toPageResponse(page);

        // Assert
        assertEquals(2, response.size());
        assertEquals(0, response.page());
        assertEquals(2, response.totalElements());
        assertTrue(response.first());
        assertTrue(response.last());
    }

}
