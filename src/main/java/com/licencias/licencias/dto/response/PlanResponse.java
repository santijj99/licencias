package com.licencias.licencias.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
public class PlanResponse {

    private Long id;
    private String nombre;
    private BigDecimal precio;
    private Integer cantidadUsuarios;
    private Integer cantidadSucursales;
    private Integer cantidadDispositivos;
    private String descripcion;
    private Boolean activo;
    private Instant createdAt;
    private Instant updatedAt;
}
