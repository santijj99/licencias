package com.licencias.licencias.repository;

import com.licencias.licencias.entity.ConexionEmpresa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ConexionEmpresaRepository extends JpaRepository<ConexionEmpresa, Long> {

    Optional<ConexionEmpresa> findByEmpresaId(Long empresaId);

    boolean existsByEmpresaId(Long empresaId);

    @Query("""
            SELECT c FROM ConexionEmpresa c
            WHERE (:empresaId IS NULL OR c.empresa.id = :empresaId)
              AND (:host IS NULL OR LOWER(c.host) LIKE LOWER(CONCAT('%', CAST(:host AS string), '%')))
            """)
    Page<ConexionEmpresa> buscar(@Param("empresaId") Long empresaId,
                                 @Param("host") String host,
                                 Pageable pageable);
}
