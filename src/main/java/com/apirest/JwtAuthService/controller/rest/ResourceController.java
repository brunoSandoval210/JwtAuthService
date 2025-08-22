package com.apirest.JwtAuthService.controller.rest;

import com.apirest.JwtAuthService.controller.dtos.resource.ResourceCreateRequest;
import com.apirest.JwtAuthService.controller.dtos.resource.ResourceResponse;
import com.apirest.JwtAuthService.controller.dtos.resource.ResourceUpdateRequest;
import com.apirest.JwtAuthService.services.interfaces.ResourceService;
import com.apirest.JwtAuthService.util.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/resources")
@RequiredArgsConstructor
@Tag(
        name = "Resource Controller",
        description = "Endpoints para administración de recursos"
)
public class ResourceController {
    private final ResourceService resourceService;

    @Operation(
            summary = "Obtener todos los recursos",
            description = "Endpoint para obtener todos los recursos. Requiere rol ADMIN y permiso de leer."
    )
    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ADMIN') and hasAuthority('READ')")
    public ResponseEntity<PageResponse<ResourceResponse>> getAllResources(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "asc") String order
    ) {
        Sort sort = order.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        return ResponseEntity.ok(resourceService.getAll(pageable));
    }

    @Operation(
            summary = "Buscar recurso por nombre",
            description = "Endpoint para buscar un recurso por su nombre. Requiere rol ADMIN y permiso de leer."
    )
    @GetMapping("/{name}")
    @PreAuthorize("hasAnyRole('ADMIN') and hasAuthority('READ')")
    public ResponseEntity<ResourceResponse> getBy(@PathVariable String name) {
        return ResponseEntity.ok(resourceService.getBy(name));
    }

    @Operation(
            summary = "Guardar un nuevo recurso",
            description = "Endpoint para crear un nuevo recurso. Requiere rol ADMIN y permiso de crear."
    )
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN') and hasAuthority('CREATE')")
    public ResponseEntity<ResourceResponse> saveResource(@RequestBody @Validated ResourceCreateRequest resourceCreateRequest, HttpServletRequest request) {
        return ResponseEntity.ok(resourceService.save(resourceCreateRequest, request));
    }

    @Operation(
            summary = "Actualizar un recurso existente",
            description = "Endpoint para actualizar un recurso. Requiere rol ADMIN y permiso de actualizar."
    )
    @PreAuthorize("hasAnyRole('ADMIN') and hasAuthority('UPDATE')")
    @PutMapping("/{resourceId}")
    public ResponseEntity<ResourceResponse> updateResource(@PathVariable Long resourceId,
                                                           @RequestBody @Validated ResourceUpdateRequest resourceUpdateRequest, HttpServletRequest request) {
        return ResponseEntity.ok(resourceService.update(resourceId, resourceUpdateRequest, request));
    }

    @Operation(
            summary = "Eliminar un recurso",
            description = "Endpoint para eliminar un recurso por su ID. Requiere rol ADMIN y permiso de eliminar."
    )
    @PreAuthorize("hasAnyRole('ADMIN') and hasAuthority('DELETE')")
    @DeleteMapping("/{resourceId}")
    public ResponseEntity<ResourceResponse> deleteResource(@PathVariable Long resourceId, HttpServletRequest request) {
        return ResponseEntity.ok(resourceService.delete(resourceId,request));
    }
}
