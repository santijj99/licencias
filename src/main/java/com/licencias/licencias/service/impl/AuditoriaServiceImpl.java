package com.licencias.licencias.service.impl;

import com.licencias.licencias.entity.Auditoria;
import com.licencias.licencias.entity.UsuarioGlobal;
import com.licencias.licencias.repository.AuditoriaRepository;
import com.licencias.licencias.repository.UsuarioGlobalRepository;
import com.licencias.licencias.security.UserPrincipal;
import com.licencias.licencias.service.AuditoriaService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.Instant;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuditoriaServiceImpl implements AuditoriaService {

    private final AuditoriaRepository auditoriaRepository;
    private final UsuarioGlobalRepository usuarioGlobalRepository;

    @Override
    @Transactional
    public void registrar(String accion, String recurso, Long recursoId, String detalle) {
        UsuarioGlobal usuario = resolveCurrentUser();
        registrar(usuario, accion, recurso, recursoId, detalle, resolveClientIp());
    }

    @Override
    @Transactional
    public void registrar(UsuarioGlobal usuario, String accion, String recurso, Long recursoId, String detalle, String ip) {
        Auditoria auditoria = Auditoria.builder()
                .usuario(usuario)
                .fecha(Instant.now())
                .accion(accion)
                .recurso(recurso)
                .recursoId(recursoId)
                .detalle(detalle)
                .ip(ip)
                .build();
        auditoriaRepository.save(auditoria);
        log.debug("Auditoría registrada: {} - {}", accion, recurso);
    }

    private UsuarioGlobal resolveCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof UserPrincipal principal)) {
            return null;
        }
        return usuarioGlobalRepository.findById(principal.getId()).orElse(null);
    }

    private String resolveClientIp() {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs == null) {
            return null;
        }
        HttpServletRequest request = attrs.getRequest();
        String forwarded = request.getHeader("X-Forwarded-For");
        if (forwarded != null && !forwarded.isBlank()) {
            return forwarded.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}
