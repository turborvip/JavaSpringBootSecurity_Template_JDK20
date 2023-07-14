package com.turborvip.security.adapter.web.rest.impl;

import com.turborvip.security.adapter.web.base.RestApiV1;
import com.turborvip.security.adapter.web.base.RestData;
import com.turborvip.security.adapter.web.base.VsResponseUtil;
import com.turborvip.security.adapter.web.rest.AuthenticationResource;
import com.turborvip.security.application.request.AuthRequest;
import com.turborvip.security.application.response.AuthResponse;
import com.turborvip.security.application.services.impl.AuthService;
import com.turborvip.security.application.services.impl.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import static com.turborvip.security.application.constants.DevMessageConstant.Common.REFRESH_TOKEN_FAIL;
import static com.turborvip.security.application.constants.DevMessageConstant.Common.REFRESH_TOKEN_SUCCESS;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpHeaders.USER_AGENT;

@RestApiV1
@RequiredArgsConstructor
public class AuthenticationResourceImpl implements AuthenticationResource {

    private final AuthService authService;
    private final JwtService jwtService;

    @Override
    public ResponseEntity<RestData<?>> login(AuthRequest authRequest, HttpServletRequest request) throws NoSuchAlgorithmException {
       return authService.authenticate(authRequest, request);
    }

    @Override
    public ResponseEntity<RestData<?>> logout(Authentication authentication, HttpServletRequest request, HttpServletResponse response) throws IOException {
        SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
        logoutHandler.logout(request, response, authentication);
        return authService.removeToken(request, response);
    }

    @Override
    public ResponseEntity<String> testAuth() {
        return ResponseEntity.ok("Hi");
    }

    @Override
    public ResponseEntity<RestData<?>> refreshToken(HttpServletRequest request) {
        try{
            String DEVICE_ID = request.getHeader(USER_AGENT);
            String authorizationHeader = request.getHeader(AUTHORIZATION);
            String token = authorizationHeader.substring("Bearer " .length());
            AuthResponse data = jwtService.generateTokenFromRefreshToken(token,DEVICE_ID);
            return VsResponseUtil.ok(REFRESH_TOKEN_SUCCESS, data);
        }catch (Exception exception){
            return VsResponseUtil.error(HttpStatus.BAD_REQUEST, REFRESH_TOKEN_FAIL);
        }
    }
}
