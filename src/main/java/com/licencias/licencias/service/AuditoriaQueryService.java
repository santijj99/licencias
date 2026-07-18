package com.licencias.licencias.service;

import com.licencias.licencias.dto.response.AuditoriaResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.Instant;

public interface AuditoriaQueryService {

    AuditoriaResponse obtenerPorId(Long id);

    Page<AuditoriaResponse> listar(Long usuarioId, String accion, Instant desde, Instant hasta, Pageable pageable);
}
