package com.licencias.licencias.service;

import com.licencias.licencias.entity.UsuarioGlobal;

public interface AuditoriaService {

    void registrar(String accion, String recurso, Long recursoId, String detalle);

    void registrar(UsuarioGlobal usuario, String accion, String recurso, Long recursoId, String detalle, String ip);
}
