package com.licencias.licencias.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class PlanRequest {

    @NotBlank
    @Size(max = 100)
    private String nombre;

    @NotNull
    @DecimalMin("0.0")
    private BigDecimal precio;

    @NotNull
    @Min(1)
    private Integer cantidadUsuarios;

    @NotNull
    @Min(1)
    private Integer cantidadSucursales;

    @NotNull
    @Min(1)
    private Integer cantidadDispositivos;

    @Size(max = 500)
    private String descripcion;

    private Boolean activo = true;
}
