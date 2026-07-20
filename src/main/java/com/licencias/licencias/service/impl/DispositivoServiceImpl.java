package com.licencias.licencias.service.impl;

import com.licencias.licencias.audit.Auditable;
import com.licencias.licencias.dto.request.DispositivoRequest;
import com.licencias.licencias.dto.response.DispositivoResponse;
import com.licencias.licencias.entity.Dispositivo;
import com.licencias.licencias.entity.Empresa;
import com.licencias.licencias.enums.TipoDispositivo;
import com.licencias.licencias.exception.ConflictException;
import com.licencias.licencias.exception.ResourceNotFoundException;
import com.licencias.licencias.mapper.DispositivoMapper;
import com.licencias.licencias.repository.DispositivoRepository;
import com.licencias.licencias.repository.EmpresaRepository;
import com.licencias.licencias.service.DispositivoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DispositivoServiceImpl implements DispositivoService {

    private final DispositivoRepository dispositivoRepository;
    private final EmpresaRepository empresaRepository;
    private final DispositivoMapper dispositivoMapper;

    @Override
    @Transactional
    @Auditable(accion = "CREAR_DISPOSITIVO", recurso = "DISPOSITIVO")
    public DispositivoResponse crear(DispositivoRequest request) {
        if (dispositivoRepository.existsByUuid(request.getUuid())) {
            throw new ConflictException("Ya existe un dispositivo con ese UUID");
        }
        Dispositivo dispositivo = dispositivoMapper.toEntity(request);
        dispositivo.setEmpresa(findEmpresa(request.getEmpresaId()));
        return dispositivoMapper.toResponse(dispositivoRepository.save(dispositivo));
    }

    @Override
    @Transactional
    @Auditable(accion = "ACTUALIZAR_DISPOSITIVO", recurso = "DISPOSITIVO")
    public DispositivoResponse actualizar(Long id, DispositivoRequest request) {
        Dispositivo dispositivo = findEntity(id);
        dispositivoRepository.findByUuid(request.getUuid())
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> {
                    throw new ConflictException("Ya existe un dispositivo con ese UUID");
                });
        dispositivoMapper.updateEntity(request, dispositivo);
        dispositivo.setEmpresa(findEmpresa(request.getEmpresaId()));
        return dispositivoMapper.toResponse(dispositivoRepository.save(dispositivo));
    }

    @Override
    @Transactional(readOnly = true)
    public DispositivoResponse obtenerPorId(Long id) {
        return dispositivoMapper.toResponse(findEntity(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DispositivoResponse> listar(String uuid, Long empresaId, TipoDispositivo tipo, Boolean activo, Pageable pageable) {
        return dispositivoRepository.buscar(uuid, empresaId, tipo, activo, pageable)
                .map(dispositivoMapper::toResponse);
    }

    @Override
    @Transactional
    @Auditable(accion = "ELIMINAR_DISPOSITIVO", recurso = "DISPOSITIVO")
    public void eliminar(Long id) {
        if (!dispositivoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Dispositivo", id);
        }
        // Hard delete: libera el UUID para que el mismo equipo pueda asociarse a otra empresa.
        dispositivoRepository.hardDeleteById(id);
    }

    private Dispositivo findEntity(Long id) {
        return dispositivoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dispositivo", id));
    }

    private Empresa findEmpresa(Long empresaId) {
        return empresaRepository.findById(empresaId)
                .orElseThrow(() -> new ResourceNotFoundException("Empresa", empresaId));
    }
}
