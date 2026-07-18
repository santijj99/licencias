package com.licencias.licencias.dto.response;

import com.licencias.licencias.enums.EstadoLicencia;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDate;

@Getter
@Setter
public class LicenciaResponse {

    private Long id;
    private String codigo;
    private Long empresaId;
    private String empresaNombre;
    private EstadoLicencia estado;
    private Integer cantidadMaximaDispositivos;
    private Integer cantidadMaximaSucursales;
    private Instant fechaCreacion;
    private LocalDate fechaVencimiento;
    private Instant createdAt;
    private Instant updatedAt;
}
