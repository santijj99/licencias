package com.licencias.licencias.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConexionEmpresaRequest {

    @NotNull
    private Long empresaId;

    @NotBlank
    @Size(max = 255)
    private String host;

    @NotNull
    @Min(1)
    @Max(65535)
    private Integer puerto;

    @NotBlank
    @Size(max = 100)
    private String databaseName;

    @NotBlank
    @Size(max = 100)
    private String username;

    /**
     * Obligatoria al crear. En actualización, si viene vacía se conserva la password actual.
     */
    @Size(max = 200)
    private String password;

    private Boolean ssl = true;
}
