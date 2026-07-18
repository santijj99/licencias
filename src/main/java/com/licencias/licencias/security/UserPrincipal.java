package com.licencias.licencias.security;

import com.licencias.licencias.entity.UsuarioGlobal;
import com.licencias.licencias.enums.RolUsuario;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
public class UserPrincipal implements UserDetails {

    private final Long id;
    private final String email;
    private final String password;
    private final String nombre;
    private final RolUsuario rol;
    private final Long empresaId;
    private final boolean activo;
    private final Collection<? extends GrantedAuthority> authorities;

    public UserPrincipal(UsuarioGlobal usuario) {
        this.id = usuario.getId();
        this.email = usuario.getEmail();
        this.password = usuario.getPassword();
        this.nombre = usuario.getNombre();
        this.rol = usuario.getRol();
        this.empresaId = usuario.getEmpresa() != null ? usuario.getEmpresa().getId() : null;
        this.activo = Boolean.TRUE.equals(usuario.getActivo());
        this.authorities = List.of(new SimpleGrantedAuthority("ROLE_" + usuario.getRol().name()));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return activo;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return activo;
    }
}
