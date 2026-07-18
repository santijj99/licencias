package com.licencias.licencias.repository;

import com.licencias.licencias.entity.Empresa;
import com.licencias.licencias.enums.EstadoEmpresa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface EmpresaRepository extends JpaRepository<Empresa, Long> {

    Optional<Empresa> findByCuit(String cuit);

    boolean existsByCuit(String cuit);

    boolean existsByEmailIgnoreCase(String email);

    @Query("""
            SELECT e FROM Empresa e
            JOIN FETCH e.plan
            WHERE e.id = :id
            """)
    Optional<Empresa> findByIdWithPlan(@Param("id") Long id);

    @Query("""
            SELECT e FROM Empresa e
            WHERE (:nombre IS NULL OR LOWER(e.nombre) LIKE LOWER(CONCAT('%', CAST(:nombre AS string), '%')))
              AND (:cuit IS NULL OR e.cuit = :cuit)
              AND (:estado IS NULL OR e.estado = :estado)
              AND (:planId IS NULL OR e.plan.id = :planId)
            """)
    Page<Empresa> buscar(@Param("nombre") String nombre,
                         @Param("cuit") String cuit,
                         @Param("estado") EstadoEmpresa estado,
                         @Param("planId") Long planId,
                         Pageable pageable);
}
