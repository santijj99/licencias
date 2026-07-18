package com.licencias.licencias.dto.response;

import com.licencias.licencias.enums.TipoDispositivo;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class DispositivoResponse {

    private Long id;
    private String uuid;
    private String nombre;
    private TipoDispositivo tipo;
    private Instant ultimoAcceso;
    private Long empresaId;
    private String empresaNombre;
    private Boolean activo;
    private Instant createdAt;
    private Instant updatedAt;
}
