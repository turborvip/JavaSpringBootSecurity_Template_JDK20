package com.turborvip.security.adapter.web.rest;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

public interface DemoResource {
    @GetMapping("/both/test")
    ResponseEntity<?> test(HttpServletRequest request);

    @GetMapping("/user/demo-auth")
    ResponseEntity<?> demoAuth(HttpServletRequest request);
}
