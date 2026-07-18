package com.licencias.licencias.service.impl;

import com.licencias.licencias.dto.request.LoginRequest;
import com.licencias.licencias.dto.request.RefreshTokenRequest;
import com.licencias.licencias.dto.response.AuthResponse;
import com.licencias.licencias.entity.RefreshToken;
import com.licencias.licencias.entity.UsuarioGlobal;
import com.licencias.licencias.exception.UnauthorizedException;
import com.licencias.licencias.repository.RefreshTokenRepository;
import com.licencias.licencias.repository.UsuarioGlobalRepository;
import com.licencias.licencias.security.JwtService;
import com.licencias.licencias.service.AuditoriaService;
import com.licencias.licencias.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.HexFormat;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UsuarioGlobalRepository usuarioGlobalRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtService jwtService;
    private final AuditoriaService auditoriaService;

    @Override
    @Transactional
    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        UsuarioGlobal usuario = usuarioGlobalRepository.findByEmailWithEmpresa(request.getEmail())
                .orElseThrow(() -> new UnauthorizedException("Credenciales inválidas"));

        if (!Boolean.TRUE.equals(usuario.getActivo())) {
            throw new UnauthorizedException("Usuario inactivo");
        }

        String accessToken = jwtService.generateAccessToken(usuario);
        String refreshToken = createRefreshToken(usuario);

        auditoriaService.registrar(usuario, "LOGIN", "AUTH", usuario.getId(), "Login exitoso", null);

        return buildAuthResponse(usuario, accessToken, refreshToken);
    }

    @Override
    @Transactional
    public AuthResponse refresh(RefreshTokenRequest request) {
        String hash = hashToken(request.getRefreshToken());
        RefreshToken stored = refreshTokenRepository.findByTokenHashAndRevokedFalse(hash)
                .orElseThrow(() -> new UnauthorizedException("Refresh token inválido"));

        if (stored.getExpiryDate().isBefore(Instant.now())) {
            stored.setRevoked(true);
            refreshTokenRepository.save(stored);
            throw new UnauthorizedException("Refresh token expirado");
        }

        stored.setRevoked(true);
        refreshTokenRepository.save(stored);

        UsuarioGlobal usuario = stored.getUsuario();
        String accessToken = jwtService.generateAccessToken(usuario);
        String newRefresh = createRefreshToken(usuario);
        return buildAuthResponse(usuario, accessToken, newRefresh);
    }

    @Override
    @Transactional
    public void logout(RefreshTokenRequest request) {
        String hash = hashToken(request.getRefreshToken());
        refreshTokenRepository.findByTokenHashAndRevokedFalse(hash).ifPresent(token -> {
            token.setRevoked(true);
            refreshTokenRepository.save(token);
            auditoriaService.registrar(token.getUsuario(), "LOGOUT", "AUTH", token.getUsuario().getId(), "Logout", null);
        });
    }

    private String createRefreshToken(UsuarioGlobal usuario) {
        String raw = jwtService.generateRefreshTokenValue();
        RefreshToken token = RefreshToken.builder()
                .tokenHash(hashToken(raw))
                .usuario(usuario)
                .expiryDate(Instant.now().plusMillis(jwtService.getRefreshTokenExpirationMs()))
                .revoked(false)
                .createdAt(Instant.now())
                .build();
        refreshTokenRepository.save(token);
        return raw;
    }

    private AuthResponse buildAuthResponse(UsuarioGlobal usuario, String accessToken, String refreshToken) {
        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(jwtService.getAccessTokenExpirationMs() / 1000)
                .usuarioId(usuario.getId())
                .email(usuario.getEmail())
                .nombre(usuario.getNombre())
                .rol(usuario.getRol())
                .empresaId(usuario.getEmpresa() != null ? usuario.getEmpresa().getId() : null)
                .build();
    }

    private String hashToken(String token) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashed = digest.digest(token.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hashed);
        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalStateException("SHA-256 no disponible", ex);
        }
    }
}
