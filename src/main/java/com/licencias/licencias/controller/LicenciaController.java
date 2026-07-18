package com.licencias.licencias.controller;

import com.licencias.licencias.dto.common.ApiResponse;
import com.licencias.licencias.dto.common.PageResponse;
import com.licencias.licencias.dto.request.LicenciaRequest;
import com.licencias.licencias.dto.request.ValidarLicenciaRequest;
import com.licencias.licencias.dto.response.LicenciaResponse;
import com.licencias.licencias.dto.response.ValidarLicenciaResponse;
import com.licencias.licencias.enums.EstadoLicencia;
import com.licencias.licencias.service.LicenciaService;
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
@RequestMapping("/api/v1/licencias")
@RequiredArgsConstructor
@Tag(name = "Licencias")
public class LicenciaController {

    private final LicenciaService licenciaService;

    @PostMapping("/validar")
    @Operation(summary = "Validar licencia y dispositivo; retorna conexión DB + JWT")
    public ResponseEntity<ApiResponse<ValidarLicenciaResponse>> validar(
            @Valid @RequestBody ValidarLicenciaRequest request) {
        return ResponseEntity.ok(ApiResponse.ok("Licencia válida", licenciaService.validar(request)));
    }

    @PostMapping
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Crear licencia")
    public ResponseEntity<ApiResponse<LicenciaResponse>> crear(@Valid @RequestBody LicenciaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Licencia creada", licenciaService.crear(request)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Actualizar licencia")
    public ResponseEntity<ApiResponse<LicenciaResponse>> actualizar(@PathVariable Long id,
                                                                    @Valid @RequestBody LicenciaRequest request) {
        return ResponseEntity.ok(ApiResponse.ok("Licencia actualizada", licenciaService.actualizar(id, request)));
    }

    @GetMapping("/{id}")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Obtener licencia por ID")
    public ResponseEntity<ApiResponse<LicenciaResponse>> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(licenciaService.obtenerPorId(id)));
    }

    @GetMapping
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Listar licencias con filtros y paginación")
    public ResponseEntity<ApiResponse<PageResponse<LicenciaResponse>>> listar(
            @RequestParam(required = false) String codigo,
            @RequestParam(required = false) Long empresaId,
            @RequestParam(required = false) EstadoLicencia estado,
            @PageableDefault(size = 20, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.ok(
                PageResponse.from(licenciaService.listar(codigo, empresaId, estado, pageable))));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Eliminar licencia (soft delete)")
    public ResponseEntity<ApiResponse<Void>> eliminar(@PathVariable Long id) {
        licenciaService.eliminar(id);
        return ResponseEntity.ok(ApiResponse.ok("Licencia eliminada", null));
    }
}
