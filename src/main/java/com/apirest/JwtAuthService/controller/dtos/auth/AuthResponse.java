package com.apirest.JwtAuthService.controller.dtos.auth;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"username", "message", "jwt", "status"})//Se define el orden de las propiedades al serializar a JSON
public record AuthResponse(
        String username,
        String message,
        String jwt,
        boolean status) {
}
