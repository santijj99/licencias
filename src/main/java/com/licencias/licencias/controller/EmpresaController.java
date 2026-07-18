package com.licencias.licencias.controller;

import com.licencias.licencias.dto.common.ApiResponse;
import com.licencias.licencias.dto.common.PageResponse;
import com.licencias.licencias.dto.request.EmpresaRequest;
import com.licencias.licencias.dto.response.EmpresaResponse;
import com.licencias.licencias.enums.EstadoEmpresa;
import com.licencias.licencias.service.EmpresaService;
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
@RequestMapping("/api/v1/empresas")
@RequiredArgsConstructor
@Tag(name = "Empresas")
@SecurityRequirement(name = "bearerAuth")
public class EmpresaController {

    private final EmpresaService empresaService;

    @PostMapping
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @Operation(summary = "Crear empresa")
    public ResponseEntity<ApiResponse<EmpresaResponse>> crear(@Valid @RequestBody EmpresaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Empresa creada", empresaService.crear(request)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN_EMPRESA')")
    @Operation(summary = "Actualizar empresa")
    public ResponseEntity<ApiResponse<EmpresaResponse>> actualizar(@PathVariable Long id,
                                                                   @Valid @RequestBody EmpresaRequest request) {
        return ResponseEntity.ok(ApiResponse.ok("Empresa actualizada", empresaService.actualizar(id, request)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener empresa por ID")
    public ResponseEntity<ApiResponse<EmpresaResponse>> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(empresaService.obtenerPorId(id)));
    }

    @GetMapping
    @Operation(summary = "Listar empresas con filtros y paginación")
    public ResponseEntity<ApiResponse<PageResponse<EmpresaResponse>>> listar(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String cuit,
            @RequestParam(required = false) EstadoEmpresa estado,
            @RequestParam(required = false) Long planId,
            @PageableDefault(size = 20, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.ok(
                PageResponse.from(empresaService.listar(nombre, cuit, estado, planId, pageable))));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @Operation(summary = "Eliminar empresa (soft delete)")
    public ResponseEntity<ApiResponse<Void>> eliminar(@PathVariable Long id) {
        empresaService.eliminar(id);
        return ResponseEntity.ok(ApiResponse.ok("Empresa eliminada", null));
    }
}
