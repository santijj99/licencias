package com.licencias.licencias.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class ConexionEmpresaResponse {

    private Long id;
    private Long empresaId;
    private String empresaNombre;
    private String host;
    private Integer puerto;
    private String databaseName;
    private String username;
    private String passwordEncriptada;
    private Boolean ssl;
    private Instant createdAt;
    private Instant updatedAt;
}
