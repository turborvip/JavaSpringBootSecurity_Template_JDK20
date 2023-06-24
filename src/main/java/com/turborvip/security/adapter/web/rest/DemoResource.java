package com.turborvip.security.adapter.web.rest;

import com.turborvip.security.domain.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

public interface DemoResource {
    @GetMapping("/both/test")
    ResponseEntity<?> test(HttpServletRequest request);

    @GetMapping("/demo-auth")
    ResponseEntity<?> demoAuth(HttpServletRequest request);
}
