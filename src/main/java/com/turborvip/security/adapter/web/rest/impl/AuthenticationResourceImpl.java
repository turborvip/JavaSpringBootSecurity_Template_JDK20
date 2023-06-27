package com.turborvip.security.adapter.web.rest.impl;

import com.turborvip.security.adapter.web.base.RestApiV1;
import com.turborvip.security.adapter.web.rest.AuthenticationResource;
import com.turborvip.security.application.constants.DevMessageConstant;
import com.turborvip.security.application.request.AuthRequest;
import com.turborvip.security.application.response.AuthResponse;
import com.turborvip.security.application.services.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

import java.io.IOException;

@RestApiV1
@RequiredArgsConstructor
public class AuthenticationResourceImpl implements AuthenticationResource {

    private final AuthService authService;

    @Override
    public ResponseEntity<AuthResponse> login(AuthRequest authRequest, HttpServletRequest request) {
        return ResponseEntity.ok(authService.authenticate(authRequest, request));
    }

    @Override
    public ResponseEntity<String> logout(Authentication authentication, HttpServletRequest request, HttpServletResponse response) throws IOException {
        SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
        logoutHandler.logout(request, response, authentication);
        authService.removeToken(request,response);
        return ResponseEntity.ok(DevMessageConstant.Common.LOGOUT);
    }

    @Override
    public ResponseEntity<String> testAuth() {
        return ResponseEntity.ok("Hi");
    }
}
