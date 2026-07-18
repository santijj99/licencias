package com.licencias.licencias.controller;

import com.licencias.licencias.dto.common.ApiResponse;
import com.licencias.licencias.dto.common.PageResponse;
import com.licencias.licencias.dto.request.DispositivoRequest;
import com.licencias.licencias.dto.response.DispositivoResponse;
import com.licencias.licencias.enums.TipoDispositivo;
import com.licencias.licencias.service.DispositivoService;
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
@RequestMapping("/api/v1/dispositivos")
@RequiredArgsConstructor
@Tag(name = "Dispositivos")
@SecurityRequirement(name = "bearerAuth")
public class DispositivoController {

    private final DispositivoService dispositivoService;

    @PostMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN_EMPRESA')")
    @Operation(summary = "Crear dispositivo")
    public ResponseEntity<ApiResponse<DispositivoResponse>> crear(@Valid @RequestBody DispositivoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Dispositivo creado", dispositivoService.crear(request)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN_EMPRESA')")
    @Operation(summary = "Actualizar dispositivo")
    public ResponseEntity<ApiResponse<DispositivoResponse>> actualizar(@PathVariable Long id,
                                                                       @Valid @RequestBody DispositivoRequest request) {
        return ResponseEntity.ok(ApiResponse.ok("Dispositivo actualizado", dispositivoService.actualizar(id, request)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener dispositivo por ID")
    public ResponseEntity<ApiResponse<DispositivoResponse>> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(dispositivoService.obtenerPorId(id)));
    }

    @GetMapping
    @Operation(summary = "Listar dispositivos con filtros y paginación")
    public ResponseEntity<ApiResponse<PageResponse<DispositivoResponse>>> listar(
            @RequestParam(required = false) String uuid,
            @RequestParam(required = false) Long empresaId,
            @RequestParam(required = false) TipoDispositivo tipo,
            @RequestParam(required = false) Boolean activo,
            @PageableDefault(size = 20, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.ok(
                PageResponse.from(dispositivoService.listar(uuid, empresaId, tipo, activo, pageable))));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN_EMPRESA')")
    @Operation(summary = "Eliminar dispositivo (soft delete)")
    public ResponseEntity<ApiResponse<Void>> eliminar(@PathVariable Long id) {
        dispositivoService.eliminar(id);
        return ResponseEntity.ok(ApiResponse.ok("Dispositivo eliminado", null));
    }
}
