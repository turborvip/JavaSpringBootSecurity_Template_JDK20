package com.turborvip.security.adapter.web.rest.impl;

import com.turborvip.security.adapter.web.base.RestApiV1;
import com.turborvip.security.adapter.web.base.RestData;
import com.turborvip.security.adapter.web.base.VsResponseUtil;
import com.turborvip.security.adapter.web.rest.DemoResource;
import com.turborvip.security.application.configuration.exception.BadRequestException;
import com.turborvip.security.application.constants.DevMessageConstant;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;

import static com.turborvip.security.application.constants.DevMessageConstant.Common.OBJECT_IS_EMPTY;


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

    @Override
    public void demoThrowException(HttpServletRequest request) {
        throw new BadRequestException("oke");
    }

    @Override
    public ResponseEntity<RestData<?>> demoSendData(HttpServletRequest request) {
        HashMap<String, String> data = new HashMap<>();
        data.put("ok", "myFriend");
        return VsResponseUtil.ok(OBJECT_IS_EMPTY, data, "Hi");
    }
}
