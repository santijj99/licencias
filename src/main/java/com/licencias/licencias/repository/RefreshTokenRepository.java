package com.licencias.licencias.repository;

import com.licencias.licencias.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByTokenHashAndRevokedFalse(String tokenHash);

    @Modifying
    @Query("UPDATE RefreshToken r SET r.revoked = true WHERE r.usuario.id = :usuarioId AND r.revoked = false")
    int revokeAllByUsuarioId(@Param("usuarioId") Long usuarioId);

    @Modifying
    @Query("DELETE FROM RefreshToken r WHERE r.expiryDate < :now OR r.revoked = true")
    int purgeExpiredOrRevoked(@Param("now") Instant now);
}
