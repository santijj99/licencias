package com.licencias.licencias.repository;

import com.licencias.licencias.entity.Dispositivo;
import com.licencias.licencias.enums.TipoDispositivo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface DispositivoRepository extends JpaRepository<Dispositivo, Long> {

    Optional<Dispositivo> findByUuid(String uuid);

    boolean existsByUuid(String uuid);

    long countByEmpresaIdAndActivoTrue(Long empresaId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = "DELETE FROM dispositivos WHERE id = :id", nativeQuery = true)
    void hardDeleteById(@Param("id") Long id);

    /** Libera UUID de filas soft-deleted para poder re-registrar el dispositivo. */
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = "DELETE FROM dispositivos WHERE uuid = :uuid AND deleted = true", nativeQuery = true)
    void hardDeleteSoftDeletedByUuid(@Param("uuid") String uuid);

    @Query("""
            SELECT d FROM Dispositivo d
            WHERE (:uuid IS NULL OR d.uuid = :uuid)
              AND (:empresaId IS NULL OR d.empresa.id = :empresaId)
              AND (:tipo IS NULL OR d.tipo = :tipo)
              AND (:activo IS NULL OR d.activo = :activo)
            """)
    Page<Dispositivo> buscar(@Param("uuid") String uuid,
                             @Param("empresaId") Long empresaId,
                             @Param("tipo") TipoDispositivo tipo,
                             @Param("activo") Boolean activo,
                             Pageable pageable);
}
