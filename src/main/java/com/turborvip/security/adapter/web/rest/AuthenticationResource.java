package com.turborvip.security.adapter.web.rest;

import com.turborvip.security.application.request.AuthRequest;
import com.turborvip.security.application.response.AuthResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface AuthenticationResource {
    @PostMapping("/auth/login")
    ResponseEntity<AuthResponse> login(@RequestBody AuthRequest authRequest);
}
