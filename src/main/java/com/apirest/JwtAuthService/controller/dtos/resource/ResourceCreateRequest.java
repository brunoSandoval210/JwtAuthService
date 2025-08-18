package com.apirest.JwtAuthService.controller.dtos.resource;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Valid
public record ResourceCreateRequest(
        @NotBlank(message = "El nombre no puede estar vacío")
        String name,
        @NotBlank(message = "La descripción no puede estar vacía")
        @Size(max = 100, message = "La descripción no puede exceder los 100 caracteres")
        String description
) {
}
