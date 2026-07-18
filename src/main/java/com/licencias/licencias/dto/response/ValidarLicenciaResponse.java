package com.licencias.licencias.dto.response;

import com.licencias.licencias.enums.EstadoLicencia;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class ValidarLicenciaResponse {

    private boolean licenciaValida;
    private EstadoLicencia estado;
    private LocalDate fechaVencimiento;
    private Integer cantidadMaximaDispositivos;
    private Integer cantidadMaximaSucursales;
    private Long empresaId;
    private String empresaNombre;
    private Long planId;
    private String planNombre;
    private String host;
    private Integer puerto;
    private String databaseName;
    private String username;
    private String passwordEncriptada;
    private Boolean ssl;
    private String accessToken;
    private String tokenType;
    private Long expiresIn;
}
