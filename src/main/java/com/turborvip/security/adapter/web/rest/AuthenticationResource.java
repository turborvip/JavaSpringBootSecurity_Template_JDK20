package com.turborvip.security.adapter.web.rest;

import com.turborvip.security.application.request.AuthRequest;
import com.turborvip.security.application.response.AuthResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public interface AuthenticationResource {
    @PostMapping("/auth/login")
    ResponseEntity<AuthResponse> login(@RequestBody AuthRequest authRequest, HttpServletRequest request) throws NoSuchAlgorithmException;

    @PostMapping("/auth/logout")
    ResponseEntity<String> logout(Authentication authentication, HttpServletRequest request, HttpServletResponse response) throws IOException;

    @PostMapping("/auth/test")
    ResponseEntity<String> testAuth();
}
