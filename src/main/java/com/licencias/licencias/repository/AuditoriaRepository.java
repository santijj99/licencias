package com.licencias.licencias.repository;

import com.licencias.licencias.entity.Auditoria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;

public interface AuditoriaRepository extends JpaRepository<Auditoria, Long> {

    @Query("""
            SELECT a FROM Auditoria a
            WHERE (:usuarioId IS NULL OR a.usuario.id = :usuarioId)
              AND (:accion IS NULL OR LOWER(a.accion) LIKE LOWER(CONCAT('%', CAST(:accion AS string), '%')))
              AND (:desde IS NULL OR a.fecha >= :desde)
              AND (:hasta IS NULL OR a.fecha <= :hasta)
            """)
    Page<Auditoria> buscar(@Param("usuarioId") Long usuarioId,
                           @Param("accion") String accion,
                           @Param("desde") Instant desde,
                           @Param("hasta") Instant hasta,
                           Pageable pageable);
}
