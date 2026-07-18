package com.licencias.licencias.repository;

import com.licencias.licencias.entity.Plan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PlanRepository extends JpaRepository<Plan, Long> {

    Optional<Plan> findByNombreIgnoreCase(String nombre);

    boolean existsByNombreIgnoreCase(String nombre);

    @Query("""
            SELECT p FROM Plan p
            WHERE (:nombre IS NULL OR LOWER(p.nombre) LIKE LOWER(CONCAT('%', CAST(:nombre AS string), '%')))
              AND (:activo IS NULL OR p.activo = :activo)
            """)
    Page<Plan> buscar(@Param("nombre") String nombre,
                      @Param("activo") Boolean activo,
                      Pageable pageable);
}
