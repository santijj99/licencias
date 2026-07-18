package com.licencias.licencias.service;

import com.licencias.licencias.dto.request.LicenciaRequest;
import com.licencias.licencias.dto.request.ValidarLicenciaRequest;
import com.licencias.licencias.dto.response.LicenciaResponse;
import com.licencias.licencias.dto.response.ValidarLicenciaResponse;
import com.licencias.licencias.enums.EstadoLicencia;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LicenciaService {

    LicenciaResponse crear(LicenciaRequest request);

    LicenciaResponse actualizar(Long id, LicenciaRequest request);

    LicenciaResponse obtenerPorId(Long id);

    Page<LicenciaResponse> listar(String codigo, Long empresaId, EstadoLicencia estado, Pageable pageable);

    void eliminar(Long id);

    ValidarLicenciaResponse validar(ValidarLicenciaRequest request);
}
