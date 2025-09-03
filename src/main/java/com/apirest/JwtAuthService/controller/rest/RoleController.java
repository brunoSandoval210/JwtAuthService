package com.apirest.JwtAuthService.controller.rest;

import com.apirest.JwtAuthService.controller.dtos.role.RoleCreateRequest;
import com.apirest.JwtAuthService.controller.dtos.role.RoleResponse;
import com.apirest.JwtAuthService.controller.dtos.role.RoleUpdateRequest;
import com.apirest.JwtAuthService.services.interfaces.RoleService;
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
@RequestMapping("/roles")
@RequiredArgsConstructor
@Tag(
        name = "Role Controller",
        description = "Endpoints para administración de roles"
)
public class RoleController {
    private final RoleService roleService;

    @Operation(
            summary = "Obtener todos los roles",
            description = "Endpoint para obtener todos los roles. Requiere rol ADMIN y permiso de lectura."
    )
    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ADMIN') and hasAuthority('READ')")
    public ResponseEntity<PageResponse<RoleResponse>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "asc") String order
    ){
        Sort sort = order.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        return ResponseEntity.ok(roleService.getAll(pageable));
    }

    @Operation(
            summary = "Obtener rol por ID",
            description = "Endpoint para obtener un rol por su ID. Requiere rol ADMIN y permiso de lectura."
    )
    @GetMapping("/{roleId}")
    @PreAuthorize("hasAnyRole('ADMIN') and hasAuthority('READ')")
    public ResponseEntity<RoleResponse> getRole(@PathVariable Long roleId){
        return ResponseEntity.ok(roleService.getById(roleId));
    }

    @Operation(
            summary = "Guardar un nuevo rol",
            description = "Endpoint para crear un nuevo rol. Requiere rol ADMIN y permiso de creación."
    )
    @PreAuthorize("hasAnyRole('ADMIN') and hasAuthority('CREATE')")
    @PostMapping
    public ResponseEntity<RoleResponse> save (@RequestBody @Validated RoleCreateRequest roleCreateRequest,
                                              HttpServletRequest request){
        return ResponseEntity.ok(roleService.save(roleCreateRequest, request));
    }

    @Operation(
            summary = "Actualizar un rol existente",
            description = "Endpoint para actualizar un rol. Requiere rol ADMIN y permiso de actualización."
    )
    @PutMapping("/{roleId}")
    @PreAuthorize("hasAnyRole('ADMIN') and hasAuthority('UPDATE')")
    public ResponseEntity<RoleResponse> update(@PathVariable Long roleId,
                                               @RequestBody @Validated RoleUpdateRequest roleUpdateRequest,
                                               HttpServletRequest request) {
        return ResponseEntity.ok(roleService.update(roleId, roleUpdateRequest, request));
    }

    @Operation(
            summary = "Eliminar un rol",
            description = "Endpoint para eliminar un rol por su ID. Requiere rol ADMIN y permiso de eliminación."
    )
    @DeleteMapping("/{roleId}")
    @PreAuthorize("hasAnyRole('ADMIN') and hasAuthority('DELETE')")
    public ResponseEntity<RoleResponse> delete(@PathVariable Long roleId, HttpServletRequest request) {
        return ResponseEntity.ok(roleService.delete(roleId, request));
    }
}
