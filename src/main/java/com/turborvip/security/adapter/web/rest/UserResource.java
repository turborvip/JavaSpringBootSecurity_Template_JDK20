package com.turborvip.security.adapter.web.rest;

import com.turborvip.security.domain.dto.UserDTO;
import com.turborvip.security.domain.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Validated
public interface UserResource {
    @PostMapping("/no-auth/create-user")
    ResponseEntity<?> create(@RequestBody User user, HttpServletRequest request);
}
