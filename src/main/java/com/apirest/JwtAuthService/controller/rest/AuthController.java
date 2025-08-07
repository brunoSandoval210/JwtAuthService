package com.apirest.JwtAuthService.controller.rest;

import com.apirest.JwtAuthService.controller.dtos.auth.AuthLoginRequest;
import com.apirest.JwtAuthService.controller.dtos.auth.AuthResponse;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @PostMapping("/login")
    @Operation(
            summary = "Autenticar usuario (simulado)",
            description = "Este endpoint es manejado internamente por un filtro. Aquí solo se documenta cómo usarlo. "
    )
    public ResponseEntity<AuthResponse> login(@RequestBody AuthLoginRequest request) {
        return null;
    }
}
