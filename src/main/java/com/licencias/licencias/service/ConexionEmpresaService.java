package com.licencias.licencias.service;

import com.licencias.licencias.dto.request.ConexionEmpresaRequest;
import com.licencias.licencias.dto.response.ConexionEmpresaResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ConexionEmpresaService {

    ConexionEmpresaResponse crear(ConexionEmpresaRequest request);

    ConexionEmpresaResponse actualizar(Long id, ConexionEmpresaRequest request);

    ConexionEmpresaResponse obtenerPorId(Long id);

    ConexionEmpresaResponse obtenerPorEmpresaId(Long empresaId);

    Page<ConexionEmpresaResponse> listar(Long empresaId, String host, Pageable pageable);

    void eliminar(Long id);
}
