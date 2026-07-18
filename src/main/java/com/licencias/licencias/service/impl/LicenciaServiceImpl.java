package com.licencias.licencias.service.impl;

import com.licencias.licencias.audit.Auditable;
import com.licencias.licencias.dto.request.LicenciaRequest;
import com.licencias.licencias.dto.request.ValidarLicenciaRequest;
import com.licencias.licencias.dto.response.LicenciaResponse;
import com.licencias.licencias.dto.response.ValidarLicenciaResponse;
import com.licencias.licencias.entity.ConexionEmpresa;
import com.licencias.licencias.entity.Dispositivo;
import com.licencias.licencias.entity.Empresa;
import com.licencias.licencias.entity.Licencia;
import com.licencias.licencias.enums.EstadoEmpresa;
import com.licencias.licencias.enums.EstadoLicencia;
import com.licencias.licencias.enums.TipoDispositivo;
import com.licencias.licencias.exception.BusinessException;
import com.licencias.licencias.exception.ConflictException;
import com.licencias.licencias.exception.LicenseValidationException;
import com.licencias.licencias.exception.ResourceNotFoundException;
import com.licencias.licencias.mapper.LicenciaMapper;
import com.licencias.licencias.repository.ConexionEmpresaRepository;
import com.licencias.licencias.repository.DispositivoRepository;
import com.licencias.licencias.repository.EmpresaRepository;
import com.licencias.licencias.repository.LicenciaRepository;
import com.licencias.licencias.security.JwtService;
import com.licencias.licencias.service.AuditoriaService;
import com.licencias.licencias.service.LicenciaService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class LicenciaServiceImpl implements LicenciaService {

    private final LicenciaRepository licenciaRepository;
    private final EmpresaRepository empresaRepository;
    private final DispositivoRepository dispositivoRepository;
    private final ConexionEmpresaRepository conexionEmpresaRepository;
    private final LicenciaMapper licenciaMapper;
    private final JwtService jwtService;
    private final AuditoriaService auditoriaService;

    @Override
    @Transactional
    @Auditable(accion = "CREAR_LICENCIA", recurso = "LICENCIA")
    public LicenciaResponse crear(LicenciaRequest request) {
        if (licenciaRepository.existsByCodigo(request.getCodigo())) {
            throw new ConflictException("Ya existe una licencia con ese código");
        }
        Empresa empresa = findEmpresa(request.getEmpresaId());
        Licencia licencia = licenciaMapper.toEntity(request);
        licencia.setEmpresa(empresa);
        licencia.setFechaCreacion(Instant.now());
        return licenciaMapper.toResponse(licenciaRepository.save(licencia));
    }

    @Override
    @Transactional
    @Auditable(accion = "ACTUALIZAR_LICENCIA", recurso = "LICENCIA")
    public LicenciaResponse actualizar(Long id, LicenciaRequest request) {
        Licencia licencia = findEntity(id);
        licenciaRepository.findByCodigo(request.getCodigo())
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> {
                    throw new ConflictException("Ya existe una licencia con ese código");
                });
        licenciaMapper.updateEntity(request, licencia);
        licencia.setEmpresa(findEmpresa(request.getEmpresaId()));
        return licenciaMapper.toResponse(licenciaRepository.save(licencia));
    }

    @Override
    @Transactional(readOnly = true)
    public LicenciaResponse obtenerPorId(Long id) {
        return licenciaMapper.toResponse(findEntity(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LicenciaResponse> listar(String codigo, Long empresaId, EstadoLicencia estado, Pageable pageable) {
        return licenciaRepository.buscar(codigo, empresaId, estado, pageable).map(licenciaMapper::toResponse);
    }

    @Override
    @Transactional
    @Auditable(accion = "ELIMINAR_LICENCIA", recurso = "LICENCIA")
    public void eliminar(Long id) {
        Licencia licencia = findEntity(id);
        licencia.softDelete();
        licenciaRepository.save(licencia);
    }

    @Override
    @Transactional
    public ValidarLicenciaResponse validar(ValidarLicenciaRequest request) {
        Licencia licencia = licenciaRepository.findByCodigoWithEmpresaAndPlan(request.getCodigoLicencia())
                .orElseThrow(() -> new LicenseValidationException("Licencia no encontrada", "LICENCIA_NO_ENCONTRADA"));

        Empresa empresa = licencia.getEmpresa();

        if (licencia.getEstado() != EstadoLicencia.ACTIVA) {
            throw new LicenseValidationException("La licencia no está activa", "LICENCIA_INACTIVA");
        }

        if (licencia.getFechaVencimiento().isBefore(LocalDate.now())
                || empresa.getFechaVencimiento().isBefore(LocalDate.now())) {
            if (licencia.getEstado() == EstadoLicencia.ACTIVA) {
                licencia.setEstado(EstadoLicencia.VENCIDA);
                licenciaRepository.save(licencia);
            }
            throw new LicenseValidationException("La licencia está vencida", "LICENCIA_VENCIDA");
        }

        if (empresa.getEstado() == EstadoEmpresa.SUSPENDIDA) {
            throw new LicenseValidationException("La empresa está suspendida", "EMPRESA_SUSPENDIDA");
        }

        if (empresa.getEstado() != EstadoEmpresa.ACTIVA) {
            throw new LicenseValidationException("La empresa no está activa", "EMPRESA_INACTIVA");
        }

        Dispositivo dispositivo = dispositivoRepository.findByUuid(request.getUuidDispositivo())
                .orElse(null);

        if (dispositivo == null) {
            long activos = dispositivoRepository.countByEmpresaIdAndActivoTrue(empresa.getId());
            if (activos >= licencia.getCantidadMaximaDispositivos()) {
                throw new LicenseValidationException(
                        "Se superó la cantidad máxima de dispositivos permitidos",
                        "MAX_DISPOSITIVOS");
            }
            dispositivo = Dispositivo.builder()
                    .uuid(request.getUuidDispositivo())
                    .nombre("Dispositivo " + request.getUuidDispositivo())
                    .tipo(TipoDispositivo.OTRO)
                    .empresa(empresa)
                    .activo(true)
                    .ultimoAcceso(Instant.now())
                    .build();
        } else {
            if (!dispositivo.getEmpresa().getId().equals(empresa.getId())) {
                throw new LicenseValidationException(
                        "El dispositivo pertenece a otra empresa",
                        "DISPOSITIVO_OTRA_EMPRESA");
            }
            if (!Boolean.TRUE.equals(dispositivo.getActivo())) {
                throw new LicenseValidationException("El dispositivo está inactivo", "DISPOSITIVO_INACTIVO");
            }
            dispositivo.setUltimoAcceso(Instant.now());
        }

        dispositivoRepository.save(dispositivo);

        ConexionEmpresa conexion = conexionEmpresaRepository.findByEmpresaId(empresa.getId())
                .orElseThrow(() -> new BusinessException("La empresa no tiene conexión de base de datos asignada"));

        String accessToken = jwtService.generateDeviceAccessToken(
                empresa.getId(), licencia.getCodigo(), request.getUuidDispositivo());

        auditoriaService.registrar(null, "VALIDAR_LICENCIA", "LICENCIA", licencia.getId(),
                "Validación OK dispositivo=" + request.getUuidDispositivo(), null);

        return ValidarLicenciaResponse.builder()
                .licenciaValida(true)
                .estado(licencia.getEstado())
                .fechaVencimiento(licencia.getFechaVencimiento())
                .cantidadMaximaDispositivos(licencia.getCantidadMaximaDispositivos())
                .cantidadMaximaSucursales(licencia.getCantidadMaximaSucursales())
                .empresaId(empresa.getId())
                .empresaNombre(empresa.getNombre())
                .planId(empresa.getPlan().getId())
                .planNombre(empresa.getPlan().getNombre())
                .host(conexion.getHost())
                .puerto(conexion.getPuerto())
                .databaseName(conexion.getDatabaseName())
                .username(conexion.getUsername())
                .passwordEncriptada(conexion.getPasswordEncriptada())
                .ssl(conexion.getSsl())
                .accessToken(accessToken)
                .tokenType("Bearer")
                .expiresIn(jwtService.getAccessTokenExpirationMs() / 1000)
                .build();
    }

    private Licencia findEntity(Long id) {
        return licenciaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Licencia", id));
    }

    private Empresa findEmpresa(Long empresaId) {
        return empresaRepository.findById(empresaId)
                .orElseThrow(() -> new ResourceNotFoundException("Empresa", empresaId));
    }
}
