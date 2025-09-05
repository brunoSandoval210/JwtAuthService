package com.apirest.JwtAuthService.services;

import com.apirest.JwtAuthService.controller.dtos.permission.PermissionCreateRequest;
import com.apirest.JwtAuthService.controller.dtos.permission.PermissionResponse;
import com.apirest.JwtAuthService.controller.dtos.permission.PermissionUpdateRequest;
import com.apirest.JwtAuthService.controller.dtos.permission.ResourcePermissionResponse;
import com.apirest.JwtAuthService.persistence.PermissionRepository;
import com.apirest.JwtAuthService.persistence.ResourceRepository;
import com.apirest.JwtAuthService.persistence.entity.Permission;
import com.apirest.JwtAuthService.persistence.entity.Resource;
import com.apirest.JwtAuthService.persistence.enums.Status;
import com.apirest.JwtAuthService.services.exception.PermissionException;
import com.apirest.JwtAuthService.services.exception.ResourceException;
import com.apirest.JwtAuthService.services.impl.PermissionServiceImpl;
import com.apirest.JwtAuthService.util.CustomMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class PermissionServiceTest {
    @Mock
    private PermissionRepository permissionRepository;

    @Mock
    private CustomMapper mapper;

    @Mock
    private ResourceRepository resourceRepository;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private PermissionServiceImpl permissionService;

    private Permission basedPermission;
    private PermissionResponse permissionResponse;

    @BeforeEach
    void setUp() {
        basedPermission = Permission.builder()
                .permissionId(1L)
                .name("permission")
                .description("permission")
                .createdAt(LocalDateTime.now())
                .usuReg("usutest")
                .resource(Resource.builder()
                        .resourceId(1L)
                        .name("resource")
                        .description("resource")
                        .usuReg("usutest")
                        .createdAt(LocalDateTime.now())
                        .status(Status.ACTIVO)
                        .build())
                .status(Status.ACTIVO)
                .build();

        permissionResponse = new PermissionResponse(
                1L,
                "permission",
                "permission",
                new ResourcePermissionResponse(
                        1L,
                        "resource",
                        "resource"
                ),
                "usutest",
                null,
                LocalDateTime.now(),
                null,
                Status.ACTIVO
        );
    }

    @Test
    @DisplayName("Test para verificar la inyeccion de dependencias")
    void contextLoads() {
        assertNotNull(permissionRepository);
        assertNotNull(mapper);
        assertNotNull(resourceRepository);
        assertNotNull(request);
    }

    @Test
    @DisplayName("Test para guardar un permiso exitosamente")
    void saveTestExit(){
        //Given
        PermissionCreateRequest permissionCreateRequest = new PermissionCreateRequest("permission","permission",1L);
        when(permissionRepository.existsByName(anyString())).thenReturn(false);
        when(resourceRepository.findById(1L)).thenReturn(Optional.of(basedPermission.getResource()));
        when(mapper.dtoToEntityPermission(permissionCreateRequest,basedPermission.getResource(),request)).thenReturn(basedPermission);
        when(permissionRepository.save(basedPermission)).thenReturn(basedPermission);
        when(mapper.entityPermissionToDto(basedPermission)).thenReturn(permissionResponse);

        //When
        PermissionResponse response = permissionService.save(permissionCreateRequest,request);
        assertNotNull(response);

        //Then
        assertEquals(1L,response.permissionId());
        assertEquals("permission",response.name());
        assertEquals("permission",response.description());

        verify(permissionRepository,times(1)).existsByName(anyString());
        verify(resourceRepository,times(1)).findById(1L);
        verify(mapper,times(1)).dtoToEntityPermission(permissionCreateRequest,basedPermission.getResource(),request);
        verify(permissionRepository,times(1)).save(basedPermission);
        verify(mapper,times(1)).entityPermissionToDto(basedPermission);
    }

    @Test
    @DisplayName("Test para guardar un permiso que ya existe")
    void saveTestFailed(){
        //Given
        PermissionCreateRequest permissionCreateRequest = new PermissionCreateRequest("permission","permission",1L);
        when(permissionRepository.existsByName(anyString())).thenReturn(true);
        //When - Then
        assertThrows(PermissionException.class, ()->{
            permissionService.save(permissionCreateRequest,request);
        });

        verify(permissionRepository,times(1)).existsByName(anyString());
        verify(resourceRepository,never()).findById(anyLong());
        verify(mapper,never()).dtoToEntityPermission(any(PermissionCreateRequest.class),any(Resource.class),eq(request));
        verify(permissionRepository,never()).save(any(Permission.class));
        verify(mapper,never()).entityPermissionToDto(any(Permission.class));
    }

    @Test
    @DisplayName("Test para guardar un permiso que su recurso no existe")
    void saveTestResourceNotExist(){
        //Given
        PermissionCreateRequest permissionCreateRequest = new PermissionCreateRequest("permission","permission",2L);
        when(permissionRepository.existsByName(anyString())).thenReturn(false);
        when(resourceRepository.findById(2L)).thenReturn(Optional.empty());

        //When - Then
        assertThrows(ResourceException.class, ()->{
            permissionService.save(permissionCreateRequest,request);
        });
        verify(permissionRepository,times(1)).existsByName(anyString());
        verify(resourceRepository,times(1)).findById(2L);
        verify(mapper,never()).dtoToEntityPermission(any(PermissionCreateRequest.class),any(Resource.class),eq(request));
        verify(permissionRepository,never()).save(any(Permission.class));
        verify(mapper,never()).entityPermissionToDto(any(Permission.class));
    }

    @Test
    @DisplayName("Test para actualizar un permiso exitosamente")
    void updateTestExit(){
        //Given
        PermissionUpdateRequest permissionUpdateRequest = new PermissionUpdateRequest("permissionUpdate","permissionUpdate",1L);
        Permission permissionUpdate = Permission.builder()
                .permissionId(1L)
                .name("permissionUpdate")
                .description("permissionUpdate")
                .createdAt(LocalDateTime.now())
                .usuReg("usutest")
                .resource(Resource.builder()
                        .resourceId(1L)
                        .name("resource")
                        .description("resource")
                        .usuReg("usutest")
                        .createdAt(LocalDateTime.now())
                        .status(Status.ACTIVO)
                        .build())
                .status(Status.ACTIVO)
                .build();

        PermissionResponse expectPermissionResponse = new PermissionResponse(
                1L,
                "permissionUpdate",
                "permissionUpdate",
                new ResourcePermissionResponse(
                        1L,
                        "resource",
                        "resource"
                ),
                "usutest",
                null,
                LocalDateTime.now(),
                null,
                Status.ACTIVO
        );

        when(permissionRepository.findById(1L)).thenReturn(Optional.of(basedPermission));
        when(resourceRepository.findById(1L)).thenReturn(Optional.of(basedPermission.getResource()));
        when(mapper.dtoToUpdatePermission(basedPermission,permissionUpdateRequest,basedPermission.getResource(),request)).thenReturn(permissionUpdate);
        when(permissionRepository.save(permissionUpdate)).thenReturn(permissionUpdate);
        when(mapper.entityPermissionToDto(permissionUpdate)).thenReturn(expectPermissionResponse);

        //When
        PermissionResponse response = permissionService.update(1L,permissionUpdateRequest,request);
        assertNotNull(response);

        //Then
        assertEquals(expectPermissionResponse.name(),response.name());
        assertEquals(expectPermissionResponse.description(),response.description());
        verify(permissionRepository,times(1)).findById(1L);
        verify(resourceRepository,times(1)).findById(anyLong());
        verify(mapper,times(1)).dtoToUpdatePermission(basedPermission,permissionUpdateRequest,basedPermission.getResource(),request);
        verify(permissionRepository,times(1)).save(permissionUpdate);
        verify(mapper,times(1)).entityPermissionToDto(permissionUpdate);
    }

    @Test
    @DisplayName("Test para actualizar un permiso inexistente")
    void updateTestPermissionNotExist(){
        //Given
        PermissionUpdateRequest permissionUpdateRequest = new PermissionUpdateRequest("permissionUpdate","permissionUpdate",1L);
        when(permissionRepository.findById(anyLong())).thenReturn(Optional.empty());
        //When - Then
        assertThrows(PermissionException.class, ()->{
           permissionService.update(2L,permissionUpdateRequest,request);
        });
        verify(permissionRepository,times(1)).findById(1L);
        verify(resourceRepository,never()).findById(anyLong());
        verify(mapper,never()).dtoToUpdatePermission(any(Permission.class),any(PermissionUpdateRequest.class),any(Resource.class),eq(request));
        verify(permissionRepository,never()).save(any(Permission.class));
        verify(mapper,never()).entityPermissionToDto(any(Permission.class));
    }

    @Test
    @DisplayName("Test para actualizar un permiso con el recurso no encontrado")
    void updateTestPermissionRecursoNotFound(){
        //Given
        PermissionUpdateRequest permissionUpdateRequest = new PermissionUpdateRequest("permissionUpdate","permissionUpdate",1L);
        when(permissionRepository.findById(1L)).thenReturn(Optional.of(basedPermission));
        when(resourceRepository.findById(anyLong())).thenReturn(Optional.empty());
        //When - Then
        assertThrows(ResourceException.class, ()->{
            permissionService.update(1L,permissionUpdateRequest,request);
        });
        verify(permissionRepository,times(1)).findById(1L);
        verify(resourceRepository,times(1)).findById(anyLong());
        verify(mapper,never()).dtoToUpdatePermission(any(Permission.class),any(PermissionUpdateRequest.class),any(Resource.class),eq(request));
        verify(permissionRepository,never()).save(any(Permission.class));
        verify(mapper,never()).entityPermissionToDto(any(Permission.class));
    }

    @Test
    @DisplayName("Test para eliminar un permiso exitoso")
    void deleteTestPermission(){
        //Given
        Permission deletedPermission = Permission.builder()
                .permissionId(1L)
                .name("permission")
                .description("permission")
                .createdAt(LocalDateTime.now())
                .usuReg("usutest")
                .resource(Resource.builder()
                        .resourceId(1L)
                        .name("resource")
                        .description("resource")
                        .usuReg("usutest")
                        .createdAt(LocalDateTime.now())
                        .status(Status.ACTIVO)
                        .build())
                .status(Status.INACTIVO)
                .usuMod("usutest")
                .updatedAt(LocalDateTime.now())
                .build();

        PermissionResponse expectedResponse = new PermissionResponse(
                1L,
                "permission",
                "permission",
                new ResourcePermissionResponse(
                        1L,
                        "resource",
                        "resource"
                ),
                "usutest",
                "usutest",
                LocalDateTime.now(),
                LocalDateTime.now(),
                Status.INACTIVO
        );
        when(permissionRepository.findById(1L)).thenReturn(Optional.of(basedPermission));
        when(permissionRepository.save(deletedPermission)).thenReturn(deletedPermission);
        when(mapper.entityPermissionToDto(deletedPermission)).thenReturn(expectedResponse);

        //When
        PermissionResponse response = permissionService.delete(1L,request);
        assertNotNull(response);
        //Then
        assertEquals(Status.INACTIVO,response.status());
        assertNotNull(response.updatedAt());
        assertNotNull(response.usuMod());

        verify(permissionRepository,times(1)).findById(1L);
        verify(mapper,times(1)).entityPermissionToDto(basedPermission);
    }

    @Test
    @DisplayName("Test para eliminar un permiso no encontrado")
    void deleteTestPermissionNotExist(){
        //Give
        when(permissionRepository.findById(anyLong())).thenReturn(Optional.empty());
        //When - Then
        assertThrows(PermissionException.class, ()->{
           permissionService.delete(1L,request);
        });

        verify(permissionRepository,times(1)).findById(1L);
        verify(mapper,never()).entityPermissionToDto(any(Permission.class));
    }
}
