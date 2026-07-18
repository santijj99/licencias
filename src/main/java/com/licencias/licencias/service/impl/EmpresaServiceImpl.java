package com.licencias.licencias.service.impl;

import com.licencias.licencias.audit.Auditable;
import com.licencias.licencias.dto.request.EmpresaRequest;
import com.licencias.licencias.dto.response.EmpresaResponse;
import com.licencias.licencias.entity.Empresa;
import com.licencias.licencias.entity.Plan;
import com.licencias.licencias.enums.EstadoEmpresa;
import com.licencias.licencias.exception.BusinessException;
import com.licencias.licencias.exception.ConflictException;
import com.licencias.licencias.exception.ResourceNotFoundException;
import com.licencias.licencias.mapper.EmpresaMapper;
import com.licencias.licencias.repository.EmpresaRepository;
import com.licencias.licencias.repository.PlanRepository;
import com.licencias.licencias.service.EmpresaService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EmpresaServiceImpl implements EmpresaService {

    private final EmpresaRepository empresaRepository;
    private final PlanRepository planRepository;
    private final EmpresaMapper empresaMapper;

    @Override
    @Transactional
    @Auditable(accion = "CREAR_EMPRESA", recurso = "EMPRESA")
    public EmpresaResponse crear(EmpresaRequest request) {
        validarUnicidad(request, null);
        Plan plan = findPlan(request.getPlanId());
        validarFechas(request);
        Empresa empresa = empresaMapper.toEntity(request);
        empresa.setPlan(plan);
        return empresaMapper.toResponse(empresaRepository.save(empresa));
    }

    @Override
    @Transactional
    @Auditable(accion = "ACTUALIZAR_EMPRESA", recurso = "EMPRESA")
    public EmpresaResponse actualizar(Long id, EmpresaRequest request) {
        Empresa empresa = findEntity(id);
        validarUnicidad(request, id);
        validarFechas(request);
        empresaMapper.updateEntity(request, empresa);
        empresa.setPlan(findPlan(request.getPlanId()));
        return empresaMapper.toResponse(empresaRepository.save(empresa));
    }

    @Override
    @Transactional(readOnly = true)
    public EmpresaResponse obtenerPorId(Long id) {
        return empresaMapper.toResponse(empresaRepository.findByIdWithPlan(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empresa", id)));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EmpresaResponse> listar(String nombre, String cuit, EstadoEmpresa estado, Long planId, Pageable pageable) {
        return empresaRepository.buscar(nombre, cuit, estado, planId, pageable).map(empresaMapper::toResponse);
    }

    @Override
    @Transactional
    @Auditable(accion = "ELIMINAR_EMPRESA", recurso = "EMPRESA")
    public void eliminar(Long id) {
        Empresa empresa = findEntity(id);
        empresa.softDelete();
        empresaRepository.save(empresa);
    }

    private void validarUnicidad(EmpresaRequest request, Long idActual) {
        empresaRepository.findByCuit(request.getCuit()).ifPresent(existing -> {
            if (idActual == null || !existing.getId().equals(idActual)) {
                throw new ConflictException("Ya existe una empresa con ese CUIT");
            }
        });
    }

    private void validarFechas(EmpresaRequest request) {
        if (request.getFechaVencimiento().isBefore(request.getFechaAlta())) {
            throw new BusinessException("La fecha de vencimiento no puede ser anterior a la fecha de alta");
        }
    }

    private Empresa findEntity(Long id) {
        return empresaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empresa", id));
    }

    private Plan findPlan(Long planId) {
        return planRepository.findById(planId)
                .orElseThrow(() -> new ResourceNotFoundException("Plan", planId));
    }
}
