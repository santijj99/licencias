package com.licencias.licencias.repository;

import com.licencias.licencias.entity.Licencia;
import com.licencias.licencias.enums.EstadoLicencia;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface LicenciaRepository extends JpaRepository<Licencia, Long> {

    Optional<Licencia> findByCodigo(String codigo);

    boolean existsByCodigo(String codigo);

    @Query("""
            SELECT l FROM Licencia l
            JOIN FETCH l.empresa e
            JOIN FETCH e.plan
            WHERE l.codigo = :codigo
            """)
    Optional<Licencia> findByCodigoWithEmpresaAndPlan(@Param("codigo") String codigo);

    @Query("""
            SELECT l FROM Licencia l
            WHERE (:codigo IS NULL OR l.codigo = :codigo)
              AND (:empresaId IS NULL OR l.empresa.id = :empresaId)
              AND (:estado IS NULL OR l.estado = :estado)
            """)
    Page<Licencia> buscar(@Param("codigo") String codigo,
                          @Param("empresaId") Long empresaId,
                          @Param("estado") EstadoLicencia estado,
                          Pageable pageable);
}
