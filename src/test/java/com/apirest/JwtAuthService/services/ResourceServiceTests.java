package com.apirest.JwtAuthService.services;

import com.apirest.JwtAuthService.controller.dtos.resource.ResourceCreateRequest;
import com.apirest.JwtAuthService.controller.dtos.resource.ResourceResponse;
import com.apirest.JwtAuthService.controller.dtos.resource.ResourceUpdateRequest;
import com.apirest.JwtAuthService.persistence.entity.Resource;
import com.apirest.JwtAuthService.persistence.ResourceRepository;
import com.apirest.JwtAuthService.persistence.enums.Status;
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
import java.util.Optional;

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

    private Resource baseResource;
    private ResourceResponse baseResourceResponse;

    @BeforeEach
    void setUp() {
        // Inicialización de los objetos base que se usan en varios tests
        baseResource = Resource.builder()
                .resourceId(1L)
                .name("testResource")
                .description("resource test")
                .usuReg("testUser")
                .createdAt(LocalDateTime.now())
                .status(Status.ACTIVO)
                .build();

        baseResourceResponse = new ResourceResponse(
                1L,
                "testResource",
                "resource test",
                null,
                "testUser",
                null,
                baseResource.getCreatedAt(),
                null,
                Status.ACTIVO
        );
    }

    @Test
    @DisplayName("Test para verificar la inyección de dependencias")
    void contextLoads() {
        assertNotNull(resourceRepository);
        assertNotNull(mapper);
        assertNotNull(resourceService);
    }

    @Test
    @DisplayName("Test para listar por nombre exitosamente")
    void  getByNameTest(){
        //Given
        when(resourceRepository.findByName("testResource")).thenReturn(Optional.of(baseResource));
        when(mapper.entityResourceToDto(baseResource)).thenReturn(baseResourceResponse);

        //When
        ResourceResponse response = resourceService.getBy("testResource");

        //Then
        assertNotNull(response);
        assertEquals("testResource",response.name());

        verify(resourceRepository,times(1)).findByName("testResource");
        verify(mapper,times(1)).entityResourceToDto(baseResource);
    }

    @Test
    @DisplayName("Test para listar por nombre no existente")
    void getByNameTestNoExist(){
        //Given
        when(resourceRepository.findByName(anyString())).thenReturn(Optional.empty());

        //When - Then
        assertThrows(ResourceException.class, () -> {
            resourceService.getBy(anyString());
        });

        verify(resourceRepository,times(1)).findByName(anyString());
        verify(mapper,never()).entityResourceToDto(any(Resource.class));
    }


    @Test
    @DisplayName("Test para crear un recurso exitosamente")
    void saveTestExist(){
        // Given
        ResourceCreateRequest baseResourceCreateRequest = new ResourceCreateRequest(
                "testResource", "resource test"
        );
        when(resourceRepository.existsByName(anyString())).thenReturn(false);
        when(mapper.dtoToEntityResource(baseResourceCreateRequest, request)).thenReturn(baseResource);
        when(resourceRepository.save(baseResource)).thenReturn(baseResource);
        when(mapper.entityResourceToDto(baseResource)).thenReturn(baseResourceResponse);

        // When
        ResourceResponse response = resourceService.save(baseResourceCreateRequest, request);

        // Then
        assertNotNull(response);
        assertEquals("testResource", response.name());
        assertEquals("resource test",response.description());

        verify(resourceRepository, times(1)).existsByName(anyString());
        verify(resourceRepository, times(1)).save(baseResource);
    }

    @Test
    @DisplayName("Test para crear un recurso que ya existe")
    void saveTestNotExist(){
        ResourceCreateRequest baseResourceCreateRequest = new ResourceCreateRequest(
                "testResource", "resource test"
        );
        // Given
        when(resourceRepository.existsByName(anyString())).thenReturn(true);

        //When - Then
        assertThrows(ResourceException.class, () ->{
           resourceService.save(baseResourceCreateRequest, request);
        });
        verify(resourceRepository, times(1)).existsByName(anyString());
        verify(resourceRepository, never()).save(any(Resource.class));
    }

    @Test
    @DisplayName("Test para actualizar un recurso exitosamente")
    void updateTestExist(){
        //Given
        ResourceUpdateRequest resourceUpdateRequest = new ResourceUpdateRequest("testUpdate","test for update entity");

        Resource updatedResource = Resource.builder()
                .resourceId(1L)
                .name("testUpdate")
                .description("test for update entity")
                .usuReg("testUser")
                .createdAt(LocalDateTime.now())
                .build();

        ResourceResponse expectedResponse = new ResourceResponse(1L, "testUpdate", "test for update entity", null, "testUser", null, null, null,Status.ACTIVO);

        when(resourceRepository.findById(1L)).thenReturn(Optional.of(baseResource));
        when(mapper.dtoToUpdateEntityResource(baseResource,resourceUpdateRequest,request)).thenReturn(updatedResource);
        when(resourceRepository.save(updatedResource)).thenReturn(updatedResource);
        when(mapper.entityResourceToDto(updatedResource)).thenReturn(expectedResponse);

        //When
        ResourceResponse response = resourceService.update(1L, resourceUpdateRequest,request);

        //Then
        assertNotNull(response);
        assertEquals("testUpdate", response.name());
        assertEquals("test for update entity",response.description());
        verify(resourceRepository,times(1)).findById(1L);
        verify(mapper,times(1)).dtoToUpdateEntityResource(baseResource,resourceUpdateRequest,request);
        verify(resourceRepository,times(1)).save(updatedResource);
        verify(mapper,times(1)).entityResourceToDto(updatedResource);
    }

    @Test
    @DisplayName("Test para actualizar un recurso que no existe")
    void updateTestNotExist(){
        //Given
        ResourceUpdateRequest resourceUpdateRequest = new ResourceUpdateRequest("testUpdate","test for update entity");
        when(resourceRepository.findById(2L)).thenReturn(Optional.empty());

        //When - Then
        assertThrows(ResourceException.class, () -> {
           resourceService.update(2L,resourceUpdateRequest,request);
        });

        verify(resourceRepository,times(1)).findById(2L);
        verify(mapper,never()).dtoToUpdateEntityResource(any(Resource.class),any(ResourceUpdateRequest.class),eq(request));
        verify(resourceRepository,never()).save(any(Resource.class));
        verify(mapper,never()).entityResourceToDto(any(Resource.class));
    }

    @Test
    @DisplayName("Test para eliminar un recurso exitoso")
    void deleteTestExist(){
        //Given
        Resource deleteResource = Resource.builder()
                .resourceId(1L)
                .name("testResource")
                .description("resource test")
                .usuReg("testUser")
                .createdAt(LocalDateTime.now())
                .status(Status.INACTIVO)
                .build();

        ResourceResponse expectedResponse = new ResourceResponse(
                1L,
                "testResource",
                "resource test",
                null,
                "testUser",
                null,
                LocalDateTime.now(),
                null,
                Status.INACTIVO
        );

        when(resourceRepository.findById(1L)).thenReturn(Optional.of(baseResource));
        when(resourceRepository.save(any(Resource.class))).thenReturn(deleteResource);
        when(mapper.entityResourceToDto(deleteResource)).thenReturn(expectedResponse);
        //When
        ResourceResponse response = resourceService.delete(1L, request);
        //Then
        assertNotNull(response);
        assertEquals(Status.INACTIVO, response.status());
        verify(resourceRepository,times(1)).findById(1L);
        verify(resourceRepository,times(1)).save(any(Resource.class));
        verify(mapper,times(1)).entityResourceToDto(any(Resource.class));
    }

    @Test
    @DisplayName("Test para eliminar un recurso que no existe")
    void deleteTestNotExist(){
        //Given
        when(resourceRepository.findById(2L)).thenReturn(Optional.empty());

        //When - Then
        assertThrows(ResourceException.class, () ->{
            resourceService.delete(2L,request);
        });

        verify(resourceRepository,times(1)).findById(2L);
        verify(resourceRepository,never()).save(any(Resource.class));
        verify(mapper,never()).entityResourceToDto(any(Resource.class));
    }
}
