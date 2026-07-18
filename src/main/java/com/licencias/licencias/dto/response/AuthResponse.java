package com.licencias.licencias.dto.response;

import com.licencias.licencias.enums.RolUsuario;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AuthResponse {

    private String accessToken;
    private String refreshToken;
    private String tokenType;
    private Long expiresIn;
    private Long usuarioId;
    private String email;
    private String nombre;
    private RolUsuario rol;
    private Long empresaId;
}
