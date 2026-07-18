package com.licencias.licencias.service.impl;

import com.licencias.licencias.audit.Auditable;
import com.licencias.licencias.dto.request.PlanRequest;
import com.licencias.licencias.dto.response.PlanResponse;
import com.licencias.licencias.entity.Plan;
import com.licencias.licencias.exception.ConflictException;
import com.licencias.licencias.exception.ResourceNotFoundException;
import com.licencias.licencias.mapper.PlanMapper;
import com.licencias.licencias.repository.PlanRepository;
import com.licencias.licencias.service.PlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PlanServiceImpl implements PlanService {

    private final PlanRepository planRepository;
    private final PlanMapper planMapper;

    @Override
    @Transactional
    @Auditable(accion = "CREAR_PLAN", recurso = "PLAN")
    public PlanResponse crear(PlanRequest request) {
        if (planRepository.existsByNombreIgnoreCase(request.getNombre())) {
            throw new ConflictException("Ya existe un plan con ese nombre");
        }
        Plan plan = planMapper.toEntity(request);
        return planMapper.toResponse(planRepository.save(plan));
    }

    @Override
    @Transactional
    @Auditable(accion = "ACTUALIZAR_PLAN", recurso = "PLAN")
    public PlanResponse actualizar(Long id, PlanRequest request) {
        Plan plan = findEntity(id);
        planRepository.findByNombreIgnoreCase(request.getNombre())
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> {
                    throw new ConflictException("Ya existe un plan con ese nombre");
                });
        planMapper.updateEntity(request, plan);
        return planMapper.toResponse(planRepository.save(plan));
    }

    @Override
    @Transactional(readOnly = true)
    public PlanResponse obtenerPorId(Long id) {
        return planMapper.toResponse(findEntity(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PlanResponse> listar(String nombre, Boolean activo, Pageable pageable) {
        return planRepository.buscar(nombre, activo, pageable).map(planMapper::toResponse);
    }

    @Override
    @Transactional
    @Auditable(accion = "ELIMINAR_PLAN", recurso = "PLAN")
    public void eliminar(Long id) {
        Plan plan = findEntity(id);
        plan.softDelete();
        planRepository.save(plan);
    }

    private Plan findEntity(Long id) {
        return planRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Plan", id));
    }
}
