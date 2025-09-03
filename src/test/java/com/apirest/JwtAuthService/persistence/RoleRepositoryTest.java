package com.apirest.JwtAuthService.persistence;

import com.apirest.JwtAuthService.persistence.entity.Permission;
import com.apirest.JwtAuthService.persistence.entity.Role;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    @BeforeEach
    void setUp() {

        Permission createPermission= Permission.builder()
                .name("CREATE")
                .description("permission test")
                .createdAt(LocalDateTime.now())
                .usuReg("test")
                .build();

        Permission readPermission= Permission.builder()
                .name("READ")
                .description("permission test")
                .createdAt(LocalDateTime.now())
                .usuReg("test")
                .build();

        Permission updatePermission= Permission.builder()
                .name("UPDATE")
                .description("permission test")
                .createdAt(LocalDateTime.now())
                .usuReg("test")
                .build();

        Permission deletePermission= Permission.builder()
                .name("DELETE")
                .description("permission test")
                .createdAt(LocalDateTime.now())
                .usuReg("test")
                .build();

        Role roleAdmin = Role.builder()
                .role("ADMIN")
                .description("role admin")
                .permissions(new HashSet<>(List.of(createPermission, readPermission, updatePermission, deletePermission)))
                .createdAt(LocalDateTime.now())
                .usuReg("test")
                .build();

        Role roleUser= Role.builder()
                .role("USER")
                .description("role user")
                .permissions(new HashSet<>(List.of(readPermission)))
                .createdAt(LocalDateTime.now())
                .usuReg("test")
                .build();

        roleRepository.saveAll(List.of(roleAdmin,roleUser));
    }


    @Test
    @DisplayName("Test para listar roles")
    void findAllTest() {
        //When
        Pageable pageable = Pageable.ofSize(10);
        Page<Role> roles = roleRepository.findAll(pageable);

        //Then
        assertNotNull(roles);
        assertEquals(2, roles.getTotalElements());
    }

    @Test
    @DisplayName("Test para buscar roles por ids")
    void findByRoleIdInTest() {
        //Given
        List<Role> roles = roleRepository.findAll();

        List<Long> roleIds = roles.stream()
                .map(Role::getRoleId)
                .toList();

        //When
        List<Role> foundRoles = roleRepository.findByRoleIdIn(roleIds);

        //Then
        assertNotNull(roles);
        assertEquals(2, roles.size());
    }

    @Test
    @DisplayName("Test para buscar rol por nombre")
    void findByRoleTest() {
        //Given
        String roleName = "ADMIN";

        //When
        Role foundRole = roleRepository.findByRole(roleName).orElse(null);

        //Then
        assertNotNull(foundRole);
        assertEquals(roleName, foundRole.getRole());
    }

    @Test
    @DisplayName("Test para crear un rol")
    void createRoleTest() {
        //Given
        Role newRole = Role.builder()
                .role("MANAGER")
                .description("role manager")
                .createdAt(LocalDateTime.now())
                .usuReg("test")
                .build();

        //When
        Role savedRole = roleRepository.save(newRole);

        //Then
        assertNotNull(savedRole);
        assertNotNull(savedRole.getRoleId());
        assertEquals("MANAGER", savedRole.getRole());
    }

    @Test
    @DisplayName("Test para actualizar un rol")
    void updateRoleTest() {
        //Given
        Optional<Role> role = roleRepository.findByRole("USER");
        assertTrue(role.isPresent());
        role.get().setDescription("updated description");

        //When
        Role updatedRole = roleRepository.save(role.get());

        //Then
        assertNotNull(updatedRole);
        assertEquals("updated description", updatedRole.getDescription());
    }

    @Test
    @DisplayName("Test para eliminar un rol")
    void deleteRoleTest() {
        //Given
        Optional<Role> role = roleRepository.findByRole("USER");
        assertTrue(role.isPresent());

        //When
        roleRepository.delete(role.get());
        Optional<Role> deletedRole = roleRepository.findByRole("USER");

        //Then
        assertFalse(deletedRole.isPresent());
    }

    @AfterEach
    void tearDown() {
        roleRepository.deleteAll();
    }

}
