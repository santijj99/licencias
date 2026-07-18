package com.licencias.licencias.controller;

import com.licencias.licencias.dto.common.ApiResponse;
import com.licencias.licencias.dto.common.PageResponse;
import com.licencias.licencias.dto.response.AuditoriaResponse;
import com.licencias.licencias.service.AuditoriaQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@RestController
@RequestMapping("/api/v1/auditorias")
@RequiredArgsConstructor
@Tag(name = "Auditorías")
@SecurityRequirement(name = "bearerAuth")
@PreAuthorize("hasRole('SUPER_ADMIN')")
public class AuditoriaController {

    private final AuditoriaQueryService auditoriaQueryService;

    @GetMapping("/{id}")
    @Operation(summary = "Obtener auditoría por ID")
    public ResponseEntity<ApiResponse<AuditoriaResponse>> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(auditoriaQueryService.obtenerPorId(id)));
    }

    @GetMapping
    @Operation(summary = "Listar auditorías con filtros y paginación")
    public ResponseEntity<ApiResponse<PageResponse<AuditoriaResponse>>> listar(
            @RequestParam(required = false) Long usuarioId,
            @RequestParam(required = false) String accion,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant desde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant hasta,
            @PageableDefault(size = 20, sort = "fecha", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.ok(
                PageResponse.from(auditoriaQueryService.listar(usuarioId, accion, desde, hasta, pageable))));
    }
}
