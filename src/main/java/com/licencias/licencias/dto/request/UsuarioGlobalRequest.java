package com.licencias.licencias.dto.request;

import com.licencias.licencias.enums.RolUsuario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioGlobalRequest {

    @NotBlank
    @Size(max = 150)
    private String nombre;

    @NotBlank
    @Email
    @Size(max = 150)
    private String email;

    @Size(min = 8, max = 100)
    private String password;

    @NotNull
    private RolUsuario rol;

    private Long empresaId;

    private Boolean activo = true;
}
