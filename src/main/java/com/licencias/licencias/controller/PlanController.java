package com.licencias.licencias.controller;

import com.licencias.licencias.dto.common.ApiResponse;
import com.licencias.licencias.dto.common.PageResponse;
import com.licencias.licencias.dto.request.PlanRequest;
import com.licencias.licencias.dto.response.PlanResponse;
import com.licencias.licencias.service.PlanService;
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
@RequestMapping("/api/v1/planes")
@RequiredArgsConstructor
@Tag(name = "Planes")
@SecurityRequirement(name = "bearerAuth")
public class PlanController {

    private final PlanService planService;

    @PostMapping
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @Operation(summary = "Crear plan")
    public ResponseEntity<ApiResponse<PlanResponse>> crear(@Valid @RequestBody PlanRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Plan creado", planService.crear(request)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @Operation(summary = "Actualizar plan")
    public ResponseEntity<ApiResponse<PlanResponse>> actualizar(@PathVariable Long id,
                                                                @Valid @RequestBody PlanRequest request) {
        return ResponseEntity.ok(ApiResponse.ok("Plan actualizado", planService.actualizar(id, request)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener plan por ID")
    public ResponseEntity<ApiResponse<PlanResponse>> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(planService.obtenerPorId(id)));
    }

    @GetMapping
    @Operation(summary = "Listar planes con filtros y paginación")
    public ResponseEntity<ApiResponse<PageResponse<PlanResponse>>> listar(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) Boolean activo,
            @PageableDefault(size = 20, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.ok(PageResponse.from(planService.listar(nombre, activo, pageable))));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @Operation(summary = "Eliminar plan (soft delete)")
    public ResponseEntity<ApiResponse<Void>> eliminar(@PathVariable Long id) {
        planService.eliminar(id);
        return ResponseEntity.ok(ApiResponse.ok("Plan eliminado", null));
    }
}
