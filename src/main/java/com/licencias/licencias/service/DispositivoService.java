package com.licencias.licencias.service;

import com.licencias.licencias.dto.request.DispositivoRequest;
import com.licencias.licencias.dto.response.DispositivoResponse;
import com.licencias.licencias.enums.TipoDispositivo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DispositivoService {

    DispositivoResponse crear(DispositivoRequest request);

    DispositivoResponse actualizar(Long id, DispositivoRequest request);

    DispositivoResponse obtenerPorId(Long id);

    Page<DispositivoResponse> listar(String uuid, Long empresaId, TipoDispositivo tipo, Boolean activo, Pageable pageable);

    void eliminar(Long id);
}
