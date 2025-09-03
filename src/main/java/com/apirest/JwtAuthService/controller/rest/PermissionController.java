package com.apirest.JwtAuthService.controller.rest;

import com.apirest.JwtAuthService.controller.dtos.permission.PermissionCreateRequest;
import com.apirest.JwtAuthService.controller.dtos.permission.PermissionResponse;
import com.apirest.JwtAuthService.controller.dtos.permission.PermissionUpdateRequest;
import com.apirest.JwtAuthService.services.interfaces.PermissionService;
import com.apirest.JwtAuthService.controller.dtos.response.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/permissions")
@RequiredArgsConstructor
@Tag(
        name = "Permission Controller",
        description = "Endpoints para administración de permisos"
)
public class PermissionController {

    private final PermissionService permissionService;

    @GetMapping("/all")
    @Operation(
            summary = "Obtener todos los permisos",
            description = "Endpoint para obtener todos los permisos. Requiere rol ADMIN y permiso de leer."
    )
    @PreAuthorize("hasAnyRole('ADMIN') and hasAuthority('READ')")
    public ResponseEntity<PageResponse<PermissionResponse>> getAllPermissions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "asc") String order
    ) {
        Sort sort = order.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        return ResponseEntity.ok(permissionService.getAll(pageable));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN') and hasAuthority('CREATE')")
    @Operation(
            summary = "Guardar un nuevo permiso",
            description = "Endpoint para crear un nuevo permiso. Requiere rol ADMIN y permiso de crear."
    )
    public ResponseEntity<PermissionResponse> savePermission(@RequestBody @Validated PermissionCreateRequest permissionCreateRequest, HttpServletRequest request){
        return ResponseEntity.ok(permissionService.save(permissionCreateRequest, request));
    }

    @PutMapping("/{permissionId}")
    @PreAuthorize("hasAnyRole('ADMIN') and hasAuthority('UPDATE')")
    @Operation(
            summary = "Actualizar un permiso existente",
            description = "Endpoint para actualizar un permiso. Requiere rol ADMIN y permiso de actualizar."
    )
    public ResponseEntity<PermissionResponse> updatePermission(@PathVariable Long permissionId,
                                                               @RequestBody @Validated PermissionUpdateRequest permissionUpdateRequest,
                                                               HttpServletRequest request) {
        return ResponseEntity.ok(permissionService.update(permissionId, permissionUpdateRequest, request));
    }

    @DeleteMapping("/{permissionId}")
    @PreAuthorize("hasAnyRole('ADMIN') and hasAuthority('DELETE')")
    @Operation(
            summary = "Eliminar un permiso",
            description = "Endpoint para eliminar un permiso. Requiere rol ADMIN y permiso de eliminar."
    )
    public ResponseEntity<PermissionResponse> deletePermission(@PathVariable Long permissionId, HttpServletRequest request) {
        return ResponseEntity.ok(permissionService.delete(permissionId, request));
    }




}
