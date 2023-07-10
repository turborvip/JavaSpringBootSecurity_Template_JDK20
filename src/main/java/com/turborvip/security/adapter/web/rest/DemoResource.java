package com.turborvip.security.adapter.web.rest;

import com.turborvip.security.adapter.web.base.RestData;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

public interface DemoResource {
    @GetMapping("/both/test")
    ResponseEntity<?> test(HttpServletRequest request);

    @GetMapping("/user/demo-auth")
    ResponseEntity<?> demoAuth(HttpServletRequest request);

    @GetMapping("/both/demo-throw-exception")
    void demoThrowException(HttpServletRequest request);

    @GetMapping("/both/demo-send-data")
    ResponseEntity<RestData<?>> demoSendData(HttpServletRequest request);
}
