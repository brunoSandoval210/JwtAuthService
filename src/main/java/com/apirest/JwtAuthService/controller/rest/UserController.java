package com.apirest.JwtAuthService.controller.rest;

import com.apirest.JwtAuthService.controller.dtos.UserReponse;
import com.apirest.JwtAuthService.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
