package com.licencias.licencias.service.impl;

import com.licencias.licencias.audit.Auditable;
import com.licencias.licencias.dto.request.ConexionEmpresaRequest;
import com.licencias.licencias.dto.response.ConexionEmpresaResponse;
import com.licencias.licencias.entity.ConexionEmpresa;
import com.licencias.licencias.entity.Empresa;
import com.licencias.licencias.exception.ConflictException;
import com.licencias.licencias.exception.ResourceNotFoundException;
import com.licencias.licencias.mapper.ConexionEmpresaMapper;
import com.licencias.licencias.repository.ConexionEmpresaRepository;
import com.licencias.licencias.repository.EmpresaRepository;
import com.licencias.licencias.service.ConexionEmpresaService;
import com.licencias.licencias.util.AesEncryptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class ConexionEmpresaServiceImpl implements ConexionEmpresaService {

    private final ConexionEmpresaRepository conexionEmpresaRepository;
    private final EmpresaRepository empresaRepository;
    private final ConexionEmpresaMapper conexionEmpresaMapper;
    private final AesEncryptionService aesEncryptionService;

    @Override
    @Transactional
    @Auditable(accion = "CREAR_CONEXION", recurso = "CONEXION")
    public ConexionEmpresaResponse crear(ConexionEmpresaRequest request) {
        if (!StringUtils.hasText(request.getPassword())) {
            throw new ConflictException("La password es obligatoria al crear la conexión");
        }
        if (conexionEmpresaRepository.existsByEmpresaId(request.getEmpresaId())) {
            throw new ConflictException("La empresa ya tiene una conexión configurada");
        }
        ConexionEmpresa conexion = conexionEmpresaMapper.toEntity(request);
        conexion.setEmpresa(findEmpresa(request.getEmpresaId()));
        conexion.setPasswordEncriptada(aesEncryptionService.encrypt(request.getPassword()));
        return conexionEmpresaMapper.toResponse(conexionEmpresaRepository.save(conexion));
    }

    @Override
    @Transactional
    @Auditable(accion = "ACTUALIZAR_CONEXION", recurso = "CONEXION")
    public ConexionEmpresaResponse actualizar(Long id, ConexionEmpresaRequest request) {
        ConexionEmpresa conexion = findEntity(id);
        if (!conexion.getEmpresa().getId().equals(request.getEmpresaId())
                && conexionEmpresaRepository.existsByEmpresaId(request.getEmpresaId())) {
            throw new ConflictException("La empresa destino ya tiene una conexión configurada");
        }
        // Actualización explícita (mismo set de campos que al crear)
        conexion.setEmpresa(findEmpresa(request.getEmpresaId()));
        conexion.setHost(request.getHost().trim());
        conexion.setPuerto(request.getPuerto());
        conexion.setDatabaseName(request.getDatabaseName().trim());
        conexion.setUsername(request.getUsername().trim());
        conexion.setSsl(request.getSsl() == null || request.getSsl());
        if (StringUtils.hasText(request.getPassword())) {
            conexion.setPasswordEncriptada(aesEncryptionService.encrypt(request.getPassword().trim()));
        }
        return conexionEmpresaMapper.toResponse(conexionEmpresaRepository.save(conexion));
    }

    @Override
    @Transactional(readOnly = true)
    public ConexionEmpresaResponse obtenerPorId(Long id) {
        return conexionEmpresaMapper.toResponse(findEntity(id));
    }

    @Override
    @Transactional(readOnly = true)
    public ConexionEmpresaResponse obtenerPorEmpresaId(Long empresaId) {
        return conexionEmpresaMapper.toResponse(conexionEmpresaRepository.findByEmpresaId(empresaId)
                .orElseThrow(() -> new ResourceNotFoundException("Conexión para empresa", empresaId)));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ConexionEmpresaResponse> listar(Long empresaId, String host, Pageable pageable) {
        return conexionEmpresaRepository.buscar(empresaId, host, pageable).map(conexionEmpresaMapper::toResponse);
    }

    @Override
    @Transactional
    @Auditable(accion = "ELIMINAR_CONEXION", recurso = "CONEXION")
    public void eliminar(Long id) {
        ConexionEmpresa conexion = findEntity(id);
        conexion.softDelete();
        conexionEmpresaRepository.save(conexion);
    }

    private ConexionEmpresa findEntity(Long id) {
        return conexionEmpresaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Conexión", id));
    }

    private Empresa findEmpresa(Long empresaId) {
        return empresaRepository.findById(empresaId)
                .orElseThrow(() -> new ResourceNotFoundException("Empresa", empresaId));
    }
}
