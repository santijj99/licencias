package com.licencias.licencias.controller;

import com.licencias.licencias.dto.common.ApiResponse;
import com.licencias.licencias.dto.common.PageResponse;
import com.licencias.licencias.dto.request.ConexionEmpresaRequest;
import com.licencias.licencias.dto.response.ConexionEmpresaResponse;
import com.licencias.licencias.service.ConexionEmpresaService;
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
@RequestMapping("/api/v1/conexiones")
@RequiredArgsConstructor
@Tag(name = "Conexiones Empresa")
@SecurityRequirement(name = "bearerAuth")
@PreAuthorize("hasRole('SUPER_ADMIN')")
public class ConexionEmpresaController {

    private final ConexionEmpresaService conexionEmpresaService;

    @PostMapping
    @Operation(summary = "Crear conexión de base de datos de empresa")
    public ResponseEntity<ApiResponse<ConexionEmpresaResponse>> crear(
            @Valid @RequestBody ConexionEmpresaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Conexión creada", conexionEmpresaService.crear(request)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar conexión")
    public ResponseEntity<ApiResponse<ConexionEmpresaResponse>> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ConexionEmpresaRequest request) {
        return ResponseEntity.ok(ApiResponse.ok("Conexión actualizada", conexionEmpresaService.actualizar(id, request)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener conexión por ID")
    public ResponseEntity<ApiResponse<ConexionEmpresaResponse>> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(conexionEmpresaService.obtenerPorId(id)));
    }

    @GetMapping("/empresa/{empresaId}")
    @Operation(summary = "Obtener conexión por empresa (host, puerto, db, user, password cifrada)")
    public ResponseEntity<ApiResponse<ConexionEmpresaResponse>> obtenerPorEmpresa(@PathVariable Long empresaId) {
        return ResponseEntity.ok(ApiResponse.ok(conexionEmpresaService.obtenerPorEmpresaId(empresaId)));
    }

    @GetMapping
    @Operation(summary = "Listar conexiones con filtros y paginación")
    public ResponseEntity<ApiResponse<PageResponse<ConexionEmpresaResponse>>> listar(
            @RequestParam(required = false) Long empresaId,
            @RequestParam(required = false) String host,
            @PageableDefault(size = 20, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.ok(
                PageResponse.from(conexionEmpresaService.listar(empresaId, host, pageable))));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar conexión (soft delete)")
    public ResponseEntity<ApiResponse<Void>> eliminar(@PathVariable Long id) {
        conexionEmpresaService.eliminar(id);
        return ResponseEntity.ok(ApiResponse.ok("Conexión eliminada", null));
    }
}
