package com.licencias.licencias.service;

import com.licencias.licencias.dto.request.EmpresaRequest;
import com.licencias.licencias.dto.response.EmpresaResponse;
import com.licencias.licencias.enums.EstadoEmpresa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EmpresaService {

    EmpresaResponse crear(EmpresaRequest request);

    EmpresaResponse actualizar(Long id, EmpresaRequest request);

    EmpresaResponse obtenerPorId(Long id);

    Page<EmpresaResponse> listar(String nombre, String cuit, EstadoEmpresa estado, Long planId, Pageable pageable);

    void eliminar(Long id);
}
