package com.licencias.licencias.service.impl;

import com.licencias.licencias.audit.Auditable;
import com.licencias.licencias.dto.request.UsuarioGlobalRequest;
import com.licencias.licencias.dto.response.UsuarioGlobalResponse;
import com.licencias.licencias.entity.Empresa;
import com.licencias.licencias.entity.UsuarioGlobal;
import com.licencias.licencias.enums.RolUsuario;
import com.licencias.licencias.exception.BusinessException;
import com.licencias.licencias.exception.ConflictException;
import com.licencias.licencias.exception.ResourceNotFoundException;
import com.licencias.licencias.mapper.UsuarioGlobalMapper;
import com.licencias.licencias.repository.EmpresaRepository;
import com.licencias.licencias.repository.UsuarioGlobalRepository;
import com.licencias.licencias.service.UsuarioGlobalService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class UsuarioGlobalServiceImpl implements UsuarioGlobalService {

    private final UsuarioGlobalRepository usuarioGlobalRepository;
    private final EmpresaRepository empresaRepository;
    private final UsuarioGlobalMapper usuarioGlobalMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    @Auditable(accion = "CREAR_USUARIO", recurso = "USUARIO")
    public UsuarioGlobalResponse crear(UsuarioGlobalRequest request) {
        if (!StringUtils.hasText(request.getPassword())) {
            throw new BusinessException("La contraseña es obligatoria al crear un usuario");
        }
        if (usuarioGlobalRepository.existsByEmailIgnoreCase(request.getEmail())) {
            throw new ConflictException("Ya existe un usuario con ese email");
        }
        UsuarioGlobal usuario = usuarioGlobalMapper.toEntity(request);
        usuario.setPassword(passwordEncoder.encode(request.getPassword()));
        usuario.setEmpresa(resolveEmpresa(request.getEmpresaId(), request.getRol()));
        return usuarioGlobalMapper.toResponse(usuarioGlobalRepository.save(usuario));
    }

    @Override
    @Transactional
    @Auditable(accion = "ACTUALIZAR_USUARIO", recurso = "USUARIO")
    public UsuarioGlobalResponse actualizar(Long id, UsuarioGlobalRequest request) {
        UsuarioGlobal usuario = findEntity(id);
        usuarioGlobalRepository.findByEmailIgnoreCase(request.getEmail())
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> {
                    throw new ConflictException("Ya existe un usuario con ese email");
                });
        usuarioGlobalMapper.updateEntity(request, usuario);
        if (StringUtils.hasText(request.getPassword())) {
            usuario.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        usuario.setEmpresa(resolveEmpresa(request.getEmpresaId(), request.getRol()));
        return usuarioGlobalMapper.toResponse(usuarioGlobalRepository.save(usuario));
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioGlobalResponse obtenerPorId(Long id) {
        return usuarioGlobalMapper.toResponse(findEntity(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UsuarioGlobalResponse> listar(String nombre, String email, RolUsuario rol, Long empresaId, Boolean activo, Pageable pageable) {
        return usuarioGlobalRepository.buscar(nombre, email, rol, empresaId, activo, pageable)
                .map(usuarioGlobalMapper::toResponse);
    }

    @Override
    @Transactional
    @Auditable(accion = "ELIMINAR_USUARIO", recurso = "USUARIO")
    public void eliminar(Long id) {
        UsuarioGlobal usuario = findEntity(id);
        usuario.softDelete();
        usuario.setActivo(false);
        usuarioGlobalRepository.save(usuario);
    }

    private Empresa resolveEmpresa(Long empresaId, RolUsuario rol) {
        if (rol == RolUsuario.SUPER_ADMIN) {
            return null;
        }
        if (empresaId == null) {
            throw new BusinessException("Los usuarios no super-admin deben pertenecer a una empresa");
        }
        return empresaRepository.findById(empresaId)
                .orElseThrow(() -> new ResourceNotFoundException("Empresa", empresaId));
    }

    private UsuarioGlobal findEntity(Long id) {
        return usuarioGlobalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", id));
    }
}
