package com.licencias.licencias.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class AuditoriaResponse {

    private Long id;
    private Long usuarioId;
    private String usuarioEmail;
    private Instant fecha;
    private String accion;
    private String ip;
    private String detalle;
    private String recurso;
    private Long recursoId;
}
