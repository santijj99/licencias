package com.licencias.licencias.service;

import com.licencias.licencias.dto.request.PlanRequest;
import com.licencias.licencias.dto.response.PlanResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PlanService {

    PlanResponse crear(PlanRequest request);

    PlanResponse actualizar(Long id, PlanRequest request);

    PlanResponse obtenerPorId(Long id);

    Page<PlanResponse> listar(String nombre, Boolean activo, Pageable pageable);

    void eliminar(Long id);
}
