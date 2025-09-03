package com.apirest.JwtAuthService.services;

import com.apirest.JwtAuthService.controller.dtos.resource.ResourceCreateRequest;
import com.apirest.JwtAuthService.controller.dtos.resource.ResourceResponse;
import com.apirest.JwtAuthService.persistence.entity.Resource;
import com.apirest.JwtAuthService.persistence.ResourceRepository;
import com.apirest.JwtAuthService.services.exception.ResourceException;
import com.apirest.JwtAuthService.services.impl.ResourceServiceImpl;
import com.apirest.JwtAuthService.util.CustomMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ResourceServiceTests {

    @Mock
    private ResourceRepository resourceRepository;

    @Mock
    private CustomMapper mapper;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private ResourceServiceImpl resourceService;

    private Resource resource;
    private ResourceResponse resourceResponse;
    private ResourceCreateRequest resourceCreateRequest;

    @BeforeEach
    void setUp() {
        resource = Resource.builder()
                .resourceId(1L)
                .name("testResource")
                .description("resource test")
                .usuReg("testUser")
                .createdAt(LocalDateTime.now())
                .build();

        resourceResponse = new ResourceResponse(
                1L, "testResource", "resource test", null,"testUser", null, LocalDateTime.now(), null
        );

        resourceCreateRequest = new ResourceCreateRequest(
                "testResource", "resource test"
        );

    }

    @Test
    @DisplayName("Test para crear un recurso exitosamente")
    void saveTestExist(){
        // Given
        when(resourceRepository.existsByName(anyString())).thenReturn(false);
        when(mapper.dtoToEntityResource(any(ResourceCreateRequest.class), any(HttpServletRequest.class))).thenReturn(resource);
        when(resourceRepository.save(any(Resource.class))).thenReturn(resource);
        when(mapper.entityResourceToDto(any(Resource.class))).thenReturn(resourceResponse);

        // When
        ResourceResponse result = resourceService.save(resourceCreateRequest, request);

        // Then
        assertNotNull(result);
        assertEquals(resourceResponse.name(), result.name());

        verify(resourceRepository, times(1)).existsByName(anyString());
        verify(resourceRepository, times(1)).save(any(Resource.class));
    }

    @Test
    @DisplayName("Test para crear un recurso que ya existe")
    void saveTestNotExist(){
        // Given
        when(resourceRepository.existsByName(anyString())).thenReturn(true);

        //When - Then
        assertThrows(ResourceException.class, () ->{
           resourceService.save(resourceCreateRequest, request);
        });
        verify(resourceRepository, times(1)).existsByName(anyString());
        verify(resourceRepository, never()).save(any(Resource.class));
    }


}
