package com.licencias.licencias.controller;

import com.licencias.licencias.dto.common.ApiResponse;
import com.licencias.licencias.dto.common.PageResponse;
import com.licencias.licencias.dto.request.UsuarioGlobalRequest;
import com.licencias.licencias.dto.response.UsuarioGlobalResponse;
import com.licencias.licencias.enums.RolUsuario;
import com.licencias.licencias.service.UsuarioGlobalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/usuarios")
@RequiredArgsConstructor
@Tag(name = "Usuarios")
@SecurityRequirement(name = "bearerAuth")
public class UsuarioGlobalController {

    private final UsuarioGlobalService usuarioGlobalService;

    @PostMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN_EMPRESA')")
    @Operation(summary = "Crear usuario global")
    public ResponseEntity<ApiResponse<UsuarioGlobalResponse>> crear(@Valid @RequestBody UsuarioGlobalRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Usuario creado", usuarioGlobalService.crear(request)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN_EMPRESA')")
    @Operation(summary = "Actualizar usuario global")
    public ResponseEntity<ApiResponse<UsuarioGlobalResponse>> actualizar(@PathVariable Long id,
                                                                         @Valid @RequestBody UsuarioGlobalRequest request) {
        return ResponseEntity.ok(ApiResponse.ok("Usuario actualizado", usuarioGlobalService.actualizar(id, request)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener usuario por ID")
    public ResponseEntity<ApiResponse<UsuarioGlobalResponse>> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(usuarioGlobalService.obtenerPorId(id)));
    }

    @GetMapping
    @Operation(summary = "Listar usuarios con filtros y paginación")
    public ResponseEntity<ApiResponse<PageResponse<UsuarioGlobalResponse>>> listar(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) RolUsuario rol,
            @RequestParam(required = false) Long empresaId,
            @RequestParam(required = false) Boolean activo,
            @PageableDefault(size = 20, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.ok(
                PageResponse.from(usuarioGlobalService.listar(nombre, email, rol, empresaId, activo, pageable))));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @Operation(summary = "Eliminar usuario (soft delete)")
    public ResponseEntity<ApiResponse<Void>> eliminar(@PathVariable Long id) {
        usuarioGlobalService.eliminar(id);
        return ResponseEntity.ok(ApiResponse.ok("Usuario eliminado", null));
    }
}
