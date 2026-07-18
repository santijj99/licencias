package com.licencias.licencias.service;

import com.licencias.licencias.dto.request.UsuarioGlobalRequest;
import com.licencias.licencias.dto.response.UsuarioGlobalResponse;
import com.licencias.licencias.enums.RolUsuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UsuarioGlobalService {

    UsuarioGlobalResponse crear(UsuarioGlobalRequest request);

    UsuarioGlobalResponse actualizar(Long id, UsuarioGlobalRequest request);

    UsuarioGlobalResponse obtenerPorId(Long id);

    Page<UsuarioGlobalResponse> listar(String nombre, String email, RolUsuario rol, Long empresaId, Boolean activo, Pageable pageable);

    void eliminar(Long id);
}
