package com.licencias.licencias.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ValidarLicenciaRequest {

    @NotBlank
    @Size(max = 64)
    private String codigoLicencia;

    @NotBlank
    @Size(max = 64)
    private String uuidDispositivo;
}
