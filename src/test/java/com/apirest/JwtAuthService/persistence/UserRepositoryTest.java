package com.apirest.JwtAuthService.persistence;

import com.apirest.JwtAuthService.persistence.entity.Permission;
import com.apirest.JwtAuthService.persistence.entity.Role;
import com.apirest.JwtAuthService.persistence.entity.User;
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
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {

			Permission createPermission= Permission.builder()
					.name("CREATE")
                    .description("Create a new user")
					.createdAt(LocalDateTime.now())
                    .usuReg("test")
					.build();

			Permission readPermission= Permission.builder()
					.name("READ")
                    .description("Read all users")
					.createdAt(LocalDateTime.now())
                    .usuReg("test")
					.build();

			Permission updatePermission= Permission.builder()
					.name("UPDATE")
                    .description("Update users")
					.createdAt(LocalDateTime.now())
                    .usuReg("test")
					.build();

			Permission deletePermission= Permission.builder()
					.name("DELETE")
                    .description("Delete users")
					.createdAt(LocalDateTime.now())
                    .usuReg("test")
					.build();

			Role roleAdmin = Role.builder()
					.role("ADMIN")
                    .description("admin")
                    .permissions(new HashSet<>(List.of(createPermission, readPermission, updatePermission, deletePermission)))
					.createdAt(LocalDateTime.now())
                    .usuReg("test")
					.build();

			Role roleUser= Role.builder()
					.role("USER")
                    .description("user")
                    .permissions(new HashSet<>(List.of(readPermission)))
					.createdAt(LocalDateTime.now())
                    .usuReg("test")
					.build();

			User user1 = User.builder()
					.username("BrunoUser")
                    .email("bruno.user@gmail.com")
					.password("$2a$10$FD3d6MJm/qFFMwZap6N2Q.KwHYjQpASB5V7SNB5L5fPibfCDS.noi")
					.isEnabled(true)//El usuario está habilitado
					.accountNoExpired(true) // La cuenta no ha expirado
					.accountNonLocked(true) // La cuenta no está bloqueada
					.credentialsNonExpired(true) // Las credenciales no han expirado
					.roles(new HashSet<>(List.of(roleUser)))
					.createdAt(LocalDateTime.now())
                    .usuReg("test")
					.build();

			User user2 = User.builder()
					.username("BrunoAdmin")
					.password("$2a$10$FD3d6MJm/qFFMwZap6N2Q.KwHYjQpASB5V7SNB5L5fPibfCDS.noi")
					.isEnabled(true)//El usuario está habilitado
					.accountNoExpired(true) // La cuenta no ha expirado
					.accountNonLocked(true) // La cuenta no está bloqueada
					.credentialsNonExpired(true) // Las credenciales no han expirado
					.roles(new HashSet<>(List.of(roleAdmin)))
					.createdAt(LocalDateTime.now())
                    .usuReg("test")
					.build();

			userRepository.saveAll(List.of(user1, user2));

    }

    @Test
    @DisplayName("Test para listar todos los usuarios")
    void testFindAll() {
        //When
        Pageable pageable = Pageable.unpaged();
        Page<User> users = userRepository.findAll(pageable);

        assertNotNull(users);
        assertEquals(2, users.getTotalElements());
    }

    @Test
    @DisplayName("Buscar por id")
    void testFindById() {
        //Given
        Long userId = userRepository.findAll().getFirst().getUserId();

        //When
        User user = userRepository.findById(userId).orElse(null);

        //Then
        assertNotNull(user);
        assertEquals(userId, user.getUserId());
    }

    @Test
    @DisplayName("Buscar por username")
    void testFindByUsername() {
        //Given
        String username = "BrunoUser";

        //When
        User user = userRepository.findByUsername(username).orElse(null);

        //Then
        assertNotNull(user);
        assertEquals(username, user.getUsername());
    }

    @Test
    @DisplayName("Existe por username")
    void testExistsByUsername() {
        //Given
        String username = "BrunoAdmin";

        //When
        boolean exists = userRepository.existsByUsername(username);

        //Then
        assertTrue(exists);
    }

    @Test
    @DisplayName("Guardar un usuario")
    void testGuardar() {
        Permission extraPermission= Permission.builder()
                    .name("EXTRA")
                    .description("Extra permission")
                    .createdAt(LocalDateTime.now())
                    .usuReg("test")
                    .build();

        Role extraRole= Role.builder()
                    .role("EXTRA_ROLE")
                    .description("Extra role")
                    .permissions(new HashSet<>(List.of(extraPermission)))
                    .createdAt(LocalDateTime.now())
                    .usuReg("test")
                    .build();
        User newUser = User.builder()
                .username("NewUser")
                .email("newuser@gmail.com")
                .password("newpassword")
                .isEnabled(true)
                .accountNoExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .roles(new HashSet<>(List.of(extraRole)))
                .createdAt(LocalDateTime.now())
                .usuReg("test")
                .build();
        //When
        User savedUser = userRepository.save(newUser);

        //Then
        assertNotNull(savedUser);
        assertNotNull(savedUser.getUserId());
        assertEquals("NewUser", savedUser.getUsername());

    }

    @Test
    @DisplayName("Actualizar un usuario")
    void testActualizar() {
        //Given
        User user = userRepository.findAll().get(0);

        //When
        User userToUpdate = userRepository.findById(user.getUserId()).orElse(null);
        assertNotNull(userToUpdate);
        userToUpdate.setUsername("UpdatedUser");
        userToUpdate.setUpdatedAt(LocalDateTime.now());
        userToUpdate.setUsuMod("test_mod");
        User updatedUser = userRepository.save(userToUpdate);

        //Then
        assertNotNull(updatedUser);
        assertEquals("UpdatedUser", updatedUser.getUsername());
    }

    @Test
    @DisplayName("Eliminar un usuario")
    void testEliminar() {
        //Given
        User user = userRepository.findAll().get(0);
        Long userId = user.getUserId();

        //When
        userRepository.deleteById(userId);
        User deletedUser = userRepository.findById(userId).orElse(null);

        //Then
        assertNull(deletedUser);
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }
}
