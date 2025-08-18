package com.apirest.JwtAuthService.controller.dtos.resource;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;

@Valid
public record ResourceUpdateRequest(
        String name,
        @Size(max = 100, message = "La descripción no puede exceder los 100 caracteres")
        String description
) {
}

