package com.apirest.JwtAuthService.controller.rest;

import com.apirest.JwtAuthService.controller.dtos.user.UserCreateRequest;
import com.apirest.JwtAuthService.controller.dtos.user.UserReponse;
import com.apirest.JwtAuthService.controller.dtos.user.UserUpdateRequest;
import com.apirest.JwtAuthService.services.interfaces.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(
        name = "User Controller",
        description = "Endpoints para administración de usuarios"
)
public class UserController {
    private final UserService userService;

    @Operation(
            summary = "Obtener todos los usuarios",
            description = "Endpoint para obtener todos los usuarios. Requiere rol ADMIN o USER y permiso de leer."
    )
    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ADMIN','USER') and hasAuthority('READ')")
    public ResponseEntity<List<UserReponse>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @Operation(
            summary = "Buscar usuario por nombre de usuario",
            description = "Endpoint para buscar un usuario por su nombre de usuario. Requiere rol ADMIN o USER y permiso de leer."
    )
    @PreAuthorize(("hasAnyRole('ADMIN','USER') and hasAuthority('READ')"))
    @GetMapping("/{username}")
    public ResponseEntity<UserReponse> getFindByUsername(@PathVariable String username) {
        return ResponseEntity.ok(userService.getFindByUsername(username));
    }

    @Operation(
            summary = "Guardar un nuevo usuario",
            description = "Endpoint para crear un nuevo usuario. Requiere rol ADMIN y permiso de crear."
    )
    @PreAuthorize("hasRole('ADMIN') and hasAuthority('CREATE')")
    @PostMapping
    public ResponseEntity<UserReponse> saveUser(@RequestBody UserCreateRequest userCreateRequest, HttpServletRequest request) {
        return ResponseEntity.ok(userService.saveUser(userCreateRequest,request));
    }

    @Operation(
            summary = "Actualizar un usuario existente",
            description = "Endpoint para actualizar un usuario. Requiere rol ADMIN y permiso de actualizar."
    )
    @PreAuthorize("hasRole('ADMIN') and hasAuthority('UPDATE')")
    @PutMapping("/{userId}")
    public ResponseEntity<UserReponse> updateUser(@PathVariable Long userId,
                                                  @RequestBody UserUpdateRequest userUpdateRequest,
                                                  HttpServletRequest request) {
        return ResponseEntity.ok(userService.updateUser(userId,userUpdateRequest,request));
    }

    @Operation(
            summary = "Eliminar un usuario",
            description = "Endpoint para eliminar un usuario por su nombre de usuario. Requiere rol ADMIN y permiso de eliminar."
    )
    @PreAuthorize("hasRole('ADMIN') and hasAuthority('DELETE')")
    @DeleteMapping("/{username}")
    public ResponseEntity<String> deleteUser(@PathVariable String username) {
        return ResponseEntity.ok(userService.deleteUser(username));
    }
}
