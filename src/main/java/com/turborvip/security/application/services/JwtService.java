package com.turborvip.security.application.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.turborvip.security.domain.entity.Token;
import com.turborvip.security.domain.entity.User;
import com.turborvip.security.domain.entity.UserDevice;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.sql.SQLOutput;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.stream.Collectors;

import static org.springframework.http.HttpHeaders.USER_AGENT;

@Service
@RequiredArgsConstructor
public class JwtService {
    @Value("${jwt_secret}")
    private String Secret_key;
    @Value("${dueTime.accessToken}")
    private Long dueTimeAccessToken;
    @Value("${dueTime.refreshToken}")
    private Long dueTimeRefreshToken;

    private final UserDeviceService userDeviceService;

    public String generateToken(User user, TokenService tokenService, Collection<SimpleGrantedAuthority> authorities, String DEVICE_ID) {
        Algorithm algorithm = Algorithm.HMAC256(Secret_key.getBytes());
        Timestamp now = new Timestamp(System.currentTimeMillis());
        Timestamp expiredTime = new Timestamp(System.currentTimeMillis() + dueTimeAccessToken * 1000);
        String jwtGenerate = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(expiredTime)
                .withClaim("roles", authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(algorithm);

        // TODO check device
        UserDevice userDevice = userDeviceService.findDeviceByUserIdAndDeviceId(user.getId(), DEVICE_ID).orElse(null);
        if (userDevice != null) {
            userDeviceService.updateLastLogin(now, userDevice.getId());
        } else {
            UserDevice userDeviceNew = new UserDevice(DEVICE_ID, now, null, null, null);
            userDeviceNew.setCreateBy(user);
            userDeviceNew.setUpdateBy(user);
            userDevice = userDeviceService.create(userDeviceNew);
        }

        Token tokenExisted = tokenService.findFirstTokenByUserIdAndTypeAndDeviceId(user.getId(), "Bear", DEVICE_ID).orElse(null);
        if (tokenExisted != null) {
            tokenService.updateTokenWithValueExpiredTime(tokenExisted.getId(), now, jwtGenerate, expiredTime);
        } else {
            Token token = new Token(null, null, "Bear", jwtGenerate, expiredTime, userDevice);
            token.setCreateBy(user);
            token.setUpdateBy(user);
            tokenService.create(token);
        }
        return jwtGenerate;
    }

    public String generateRefreshToken(User user, TokenService tokenService, Collection<SimpleGrantedAuthority> authorities, String DEVICE_ID) {
        Algorithm algorithm = Algorithm.HMAC256(Secret_key.getBytes());
        Timestamp now = new Timestamp(System.currentTimeMillis());
        Timestamp expiredTime = new Timestamp(System.currentTimeMillis() + dueTimeRefreshToken * 1000);

        String jwtGenerate = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(expiredTime)
                .sign(algorithm);

        // TODO check device
        UserDevice userDevice = userDeviceService.findDeviceByUserIdAndDeviceId(user.getId(), DEVICE_ID).orElse(null);
        if (userDevice != null) {
            userDeviceService.updateLastLogin(now, userDevice.getId());
        } else {
            UserDevice userDeviceNew = new UserDevice(DEVICE_ID, now, null, null, null);
            userDeviceNew.setCreateBy(user);
            userDeviceNew.setUpdateBy(user);
            userDevice = userDeviceService.create(userDeviceNew);
        }

        Token tokenExisted = tokenService.findFirstTokenByUserIdAndTypeAndDeviceId(user.getId(), "Refresh", DEVICE_ID).orElse(null);
        if (tokenExisted != null) {
            tokenService.updateTokenWithValueExpiredTime(tokenExisted.getId(), now, jwtGenerate, expiredTime);
        } else {
            Token token = new Token(null, null, "Refresh", jwtGenerate, expiredTime, userDevice);
            token.setCreateBy(user);
            token.setUpdateBy(user);
            tokenService.create(token);
        }
        return jwtGenerate;
    }
}
