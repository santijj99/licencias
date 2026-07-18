package com.licencias.licencias.service.impl;

import com.licencias.licencias.dto.response.AuditoriaResponse;
import com.licencias.licencias.exception.ResourceNotFoundException;
import com.licencias.licencias.mapper.AuditoriaMapper;
import com.licencias.licencias.repository.AuditoriaRepository;
import com.licencias.licencias.service.AuditoriaQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class AuditoriaQueryServiceImpl implements AuditoriaQueryService {

    private final AuditoriaRepository auditoriaRepository;
    private final AuditoriaMapper auditoriaMapper;

    @Override
    @Transactional(readOnly = true)
    public AuditoriaResponse obtenerPorId(Long id) {
        return auditoriaMapper.toResponse(auditoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Auditoría", id)));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AuditoriaResponse> listar(Long usuarioId, String accion, Instant desde, Instant hasta, Pageable pageable) {
        return auditoriaRepository.buscar(usuarioId, accion, desde, hasta, pageable).map(auditoriaMapper::toResponse);
    }
}
