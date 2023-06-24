package com.turborvip.security.adapter.web.rest.impl;

import com.turborvip.security.adapter.web.base.RestApiV1;
import com.turborvip.security.adapter.web.rest.AuthenticationResource;
import com.turborvip.security.application.request.AuthRequest;
import com.turborvip.security.application.response.AuthResponse;
import com.turborvip.security.application.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;

@RestApiV1
@RequiredArgsConstructor
public class AuthenticationResourceImpl implements AuthenticationResource {

    private final AuthService authService;
    @Override
    public ResponseEntity<AuthResponse> login(AuthRequest authRequest) {
        return ResponseEntity.ok(authService.authenticate(authRequest));
    }
}
