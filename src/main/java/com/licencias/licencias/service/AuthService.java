package com.licencias.licencias.service;

import com.licencias.licencias.dto.request.LoginRequest;
import com.licencias.licencias.dto.request.RefreshTokenRequest;
import com.licencias.licencias.dto.response.AuthResponse;

public interface AuthService {

    AuthResponse login(LoginRequest request);

    AuthResponse refresh(RefreshTokenRequest request);

    void logout(RefreshTokenRequest request);
}
