package com.turborvip.security.adapter.web.rest.impl;

import com.turborvip.security.adapter.web.base.RestApiV1;
import com.turborvip.security.adapter.web.rest.DemoResource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

@RestApiV1
public class DemoResourceImpl implements DemoResource {
    @Override
    public ResponseEntity<?> test(HttpServletRequest request) {
        return ResponseEntity.ok("Server is running!");
    }

    @Override
    public ResponseEntity<?> demoAuth(HttpServletRequest request) {
        return ResponseEntity.ok("Authentication & Authorization is successfully!");
    }
}
