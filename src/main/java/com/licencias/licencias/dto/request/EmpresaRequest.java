package com.licencias.licencias.dto.request;

import com.licencias.licencias.enums.EstadoEmpresa;
import com.licencias.licencias.validation.ValidCuit;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class EmpresaRequest {

    @NotBlank
    @Size(max = 200)
    private String nombre;

    @NotBlank
    @ValidCuit
    @Size(max = 20)
    private String cuit;

    @NotBlank
    @Email
    @Size(max = 150)
    private String email;

    @Size(max = 50)
    private String telefono;

    private EstadoEmpresa estado = EstadoEmpresa.ACTIVA;

    @NotNull
    private Long planId;

    @NotNull
    private LocalDate fechaAlta;

    @NotNull
    private LocalDate fechaVencimiento;
}
