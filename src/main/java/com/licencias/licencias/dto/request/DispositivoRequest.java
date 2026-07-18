package com.licencias.licencias.dto.request;

import com.licencias.licencias.enums.TipoDispositivo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DispositivoRequest {

    @NotBlank
    @Size(max = 64)
    private String uuid;

    @NotBlank
    @Size(max = 150)
    private String nombre;

    @NotNull
    private TipoDispositivo tipo;

    @NotNull
    private Long empresaId;

    private Boolean activo = true;
}
