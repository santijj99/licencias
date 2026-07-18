package com.licencias.licencias.dto.request;

import com.licencias.licencias.enums.EstadoLicencia;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class LicenciaRequest {

    @NotBlank
    @Size(max = 64)
    private String codigo;

    @NotNull
    private Long empresaId;

    private EstadoLicencia estado = EstadoLicencia.ACTIVA;

    @NotNull
    @Min(1)
    private Integer cantidadMaximaDispositivos;

    @NotNull
    @Min(1)
    private Integer cantidadMaximaSucursales;

    @NotNull
    private LocalDate fechaVencimiento;
}
