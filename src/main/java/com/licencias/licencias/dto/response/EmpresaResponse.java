package com.licencias.licencias.dto.response;

import com.licencias.licencias.enums.EstadoEmpresa;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDate;

@Getter
@Setter
public class EmpresaResponse {

    private Long id;
    private String nombre;
    private String cuit;
    private String email;
    private String telefono;
    private EstadoEmpresa estado;
    private Long planId;
    private String planNombre;
    private LocalDate fechaAlta;
    private LocalDate fechaVencimiento;
    private Instant createdAt;
    private Instant updatedAt;
}
