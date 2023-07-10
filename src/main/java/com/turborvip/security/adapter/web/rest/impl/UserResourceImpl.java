package com.turborvip.security.adapter.web.rest.impl;

import com.turborvip.security.adapter.web.base.RestApiV1;
import com.turborvip.security.adapter.web.base.VsResponseUtil;
import com.turborvip.security.adapter.web.rest.UserResource;
import com.turborvip.security.application.services.UserService;
import com.turborvip.security.domain.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;

@RestApiV1
@RequiredArgsConstructor
public class UserResourceImpl implements UserResource {
    private final UserService userService;

    @Override
    public ResponseEntity<?> create(User user, HttpServletRequest request) {
        return VsResponseUtil.ok(null, userService.create(user, request), null);
    }
}
