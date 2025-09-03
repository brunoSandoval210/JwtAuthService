package com.apirest.JwtAuthService.persistence;

import com.apirest.JwtAuthService.persistence.entity.Permission;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
public class PermissionRepositoryTest {

    @Autowired
    private PermissionRepository permissionRepository;

    @Test
    @DisplayName("Test para listar todos los permisos")
    void findAllTest(){

        //Given
        Permission permission1 = Permission.builder()
                .name("test1")
                .description("permission test")
                .createdAt(LocalDateTime.now())
                .usuReg("test")
                .build();

        Permission permission2 = Permission.builder()
                .name("test2")
                .description("permission test")
                .createdAt(LocalDateTime.now())
                .usuReg("test")
                .build();

        Permission permission3 = Permission.builder()
                .name("test3")
                .description("permission test")
                .createdAt(LocalDateTime.now())
                .usuReg("test")
                .build();

        permissionRepository.saveAll(List.of(permission1, permission2, permission3));

        Pageable pageable = PageRequest.of(0, 10);

        //When
        Page<Permission> permissions = permissionRepository.findAll(pageable);

        //Then
        assertNotNull(permissions);
        assertEquals(3, permissions.getTotalElements());
        assertFalse(permissions.isEmpty());

    }

    @Test
    @DisplayName("Test para encontrar un permiso por su ID")
    void findByIdTest() {
        // Given
        Permission permission = Permission.builder()
                .name("test1")
                .description("permission test")
                .createdAt(LocalDateTime.now())
                .usuReg("test")
                .build();

        Permission savedPermission = permissionRepository.save(permission);

        // When
        Permission foundPermission = permissionRepository.findById(savedPermission.getPermissionId()).orElse(null);

        // Then
        assertNotNull(foundPermission, "El permiso no debe ser nulo");
        assertEquals(savedPermission.getPermissionId(), foundPermission.getPermissionId(), "Los IDs deben coincidir");
        assertEquals("test1", foundPermission.getName(), "El nombre del permiso debe ser 'test1'");
    }

    @Test
    @DisplayName("Test para verificar la existencia de un permiso por su nombre")
    void existsByNameTest() {
        // Given
        Permission permission = Permission.builder()
                .name("uniquePermission")
                .description("permission test")
                .createdAt(LocalDateTime.now())
                .usuReg("test")
                .build();

        permissionRepository.save(permission);

        // When
        boolean exists = permissionRepository.existsByName("uniquePermission");
        boolean notExists = permissionRepository.existsByName("nonExistentPermission");

        // Then
        assertTrue(exists, "El permiso con nombre 'uniquePermission' debe existir");
        assertFalse(notExists, "El permiso con nombre 'nonExistentPermission' no debe existir");
    }

    @Test
    @DisplayName("Buscar permisos por una lista de IDs")
    void findByIdsTest() {
        // Given
        Permission permission1 = Permission.builder()
                .name("test1")
                .description("permission test")
                .createdAt(LocalDateTime.now())
                .usuReg("test")
                .build();

        Permission permission2 = Permission.builder()
                .name("test2")
                .description("permission test")
                .createdAt(LocalDateTime.now())
                .usuReg("test")
                .build();

        Permission permission3 = Permission.builder()
                .name("test3")
                .description("permission test")
                .createdAt(LocalDateTime.now())
                .usuReg("test")
                .build();

        permissionRepository.saveAll(List.of(permission1, permission2, permission3));

        // When
        List<Permission> foundPermissions = permissionRepository.findByPermissionIdIn(
                List.of(permission1.getPermissionId(), permission3.getPermissionId())
        );

        // Then
        assertNotNull(foundPermissions, "La lista de permisos no debe ser nula");
        assertEquals(2, foundPermissions.size(), "Deben encontrarse 2 permisos");
        assertTrue(foundPermissions.stream().anyMatch(p -> p.getPermissionId().equals(permission1.getPermissionId())), "Debe contener el permiso1");
        assertTrue(foundPermissions.stream().anyMatch(p -> p.getPermissionId().equals(permission3.getPermissionId())), "Debe contener el permiso3");
    }

    @Test
    @DisplayName("Test para guardar un permiso")
    void savePermissionTest() {
        // Given
        Permission permission = Permission.builder()
                .name("saveTest")
                .description("permission test")
                .createdAt(LocalDateTime.now())
                .usuReg("test")
                .build();

        // When
        Permission savedPermission = permissionRepository.save(permission);

        // Then
        assertNotNull(savedPermission, "El permiso guardado no debe ser nulo");
        assertNotNull(savedPermission.getPermissionId(), "El ID del permiso guardado no debe ser nulo");
        assertEquals("saveTest", savedPermission.getName(), "El nombre del permiso debe ser 'saveTest'");
    }

    @Test
    @DisplayName("Test para actualizar un permiso")
    void updatePermissionTest() {
        // Given
        Permission permission = Permission.builder()
                .name("updateTest")
                .description("permission test")
                .createdAt(LocalDateTime.now())
                .usuReg("test")
                .build();

        Permission savedPermission = permissionRepository.save(permission);

        // When
        savedPermission.setDescription("updated description");
        Permission updatedPermission = permissionRepository.save(savedPermission);

        // Then
        assertNotNull(updatedPermission, "El permiso actualizado no debe ser nulo");
        assertEquals(savedPermission.getPermissionId(), updatedPermission.getPermissionId(), "Los IDs deben coincidir");
        assertEquals("updated description", updatedPermission.getDescription(), "La descripción debe ser actualizada");
    }

    @Test
    @DisplayName("Test para eliminar un permiso")
    void deletePermissionTest() {
        // Given
        Permission permission = Permission.builder()
                .name("deleteTest")
                .description("permission test")
                .createdAt(LocalDateTime.now())
                .usuReg("test")
                .build();

        Permission savedPermission = permissionRepository.save(permission);
        Long permissionId = savedPermission.getPermissionId();

        // When
        permissionRepository.deleteById(permissionId);
        Permission deletedPermission = permissionRepository.findById(permissionId).orElse(null);

        // Then
        assertNull(deletedPermission, "El permiso debe ser eliminado y no debe encontrarse");
    }
}
