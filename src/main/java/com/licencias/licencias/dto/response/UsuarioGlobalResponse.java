package com.licencias.licencias.dto.response;

import com.licencias.licencias.enums.RolUsuario;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class UsuarioGlobalResponse {

    private Long id;
    private String nombre;
    private String email;
    private RolUsuario rol;
    private Long empresaId;
    private String empresaNombre;
    private Boolean activo;
    private Instant createdAt;
    private Instant updatedAt;
}
