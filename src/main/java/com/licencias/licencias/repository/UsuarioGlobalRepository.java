package com.licencias.licencias.repository;

import com.licencias.licencias.entity.UsuarioGlobal;
import com.licencias.licencias.enums.RolUsuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UsuarioGlobalRepository extends JpaRepository<UsuarioGlobal, Long> {

    Optional<UsuarioGlobal> findByEmailIgnoreCase(String email);

    boolean existsByEmailIgnoreCase(String email);

    @Query("""
            SELECT u FROM UsuarioGlobal u
            LEFT JOIN FETCH u.empresa
            WHERE LOWER(u.email) = LOWER(:email)
            """)
    Optional<UsuarioGlobal> findByEmailWithEmpresa(@Param("email") String email);

    @Query("""
            SELECT u FROM UsuarioGlobal u
            WHERE (:nombre IS NULL OR LOWER(u.nombre) LIKE LOWER(CONCAT('%', CAST(:nombre AS string), '%')))
              AND (:email IS NULL OR LOWER(u.email) LIKE LOWER(CONCAT('%', CAST(:email AS string), '%')))
              AND (:rol IS NULL OR u.rol = :rol)
              AND (:empresaId IS NULL OR u.empresa.id = :empresaId)
              AND (:activo IS NULL OR u.activo = :activo)
            """)
    Page<UsuarioGlobal> buscar(@Param("nombre") String nombre,
                               @Param("email") String email,
                               @Param("rol") RolUsuario rol,
                               @Param("empresaId") Long empresaId,
                               @Param("activo") Boolean activo,
                               Pageable pageable);
}
