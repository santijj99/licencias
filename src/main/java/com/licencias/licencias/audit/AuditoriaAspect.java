package com.licencias.licencias.audit;

import com.licencias.licencias.service.AuditoriaService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
@RequiredArgsConstructor
public class AuditoriaAspect {

    private final AuditoriaService auditoriaService;

    @AfterReturning(pointcut = "@annotation(auditable)", returning = "result")
    public void afterAuditable(JoinPoint joinPoint, Auditable auditable, Object result) {
        Long recursoId = extractId(result);
        String detalle = joinPoint.getSignature().toShortString();
        auditoriaService.registrar(auditable.accion(), auditable.recurso(), recursoId, detalle);
    }

    private Long extractId(Object result) {
        if (result == null) {
            return null;
        }
        try {
            Method method = result.getClass().getMethod("getId");
            Object value = method.invoke(result);
            return value instanceof Long id ? id : null;
        } catch (Exception ex) {
            return null;
        }
    }
}
