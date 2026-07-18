package com.licencias.licencias.security;

import com.licencias.licencias.config.JwtProperties;
import com.licencias.licencias.entity.UsuarioGlobal;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

@Service
public class JwtService {

    private final JwtProperties jwtProperties;
    private final SecretKey signingKey;

    public JwtService(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
        this.signingKey = buildKey(jwtProperties.getSecret());
    }

    public String generateAccessToken(UsuarioGlobal usuario) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("uid", usuario.getId());
        claims.put("rol", usuario.getRol().name());
        if (usuario.getEmpresa() != null) {
            claims.put("empresaId", usuario.getEmpresa().getId());
        }
        return buildToken(claims, usuario.getEmail(), jwtProperties.getAccessTokenExpirationMs());
    }

    public String generateDeviceAccessToken(Long empresaId, String codigoLicencia, String deviceUuid) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("tipo", "DEVICE");
        claims.put("empresaId", empresaId);
        claims.put("licencia", codigoLicencia);
        claims.put("deviceUuid", deviceUuid);
        return buildToken(claims, "device:" + deviceUuid, jwtProperties.getAccessTokenExpirationMs());
    }

    public String generateRefreshTokenValue() {
        return UUID.randomUUID() + "." + UUID.randomUUID();
    }

    public long getAccessTokenExpirationMs() {
        return jwtProperties.getAccessTokenExpirationMs();
    }

    public long getRefreshTokenExpirationMs() {
        return jwtProperties.getRefreshTokenExpirationMs();
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public boolean isTokenValid(String token, UserPrincipal principal) {
        String username = extractUsername(token);
        return username.equals(principal.getUsername()) && !isTokenExpired(token);
    }

    public boolean isTokenValid(String token) {
        try {
            return !isTokenExpired(token);
        } catch (Exception ex) {
            return false;
        }
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private <T> T extractClaim(String token, Function<Claims, T> resolver) {
        return resolver.apply(extractAllClaims(token));
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private String buildToken(Map<String, Object> claims, String subject, long expirationMs) {
        Date now = new Date();
        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .issuedAt(now)
                .expiration(new Date(now.getTime() + expirationMs))
                .signWith(signingKey)
                .compact();
    }

    private SecretKey buildKey(String secret) {
        byte[] keyBytes;
        try {
            keyBytes = Decoders.BASE64.decode(secret);
        } catch (Exception ex) {
            keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        }
        return Keys.hmacShaKeyFor(keyBytes.length >= 32 ? keyBytes : padKey(keyBytes));
    }

    private byte[] padKey(byte[] keyBytes) {
        byte[] padded = new byte[32];
        System.arraycopy(keyBytes, 0, padded, 0, Math.min(keyBytes.length, 32));
        return padded;
    }
}
