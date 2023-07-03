package com.turborvip.security.application.services;

import com.turborvip.security.application.repositories.TokenRepository;
import com.turborvip.security.domain.entity.Token;
import com.turborvip.security.domain.entity.User;
import com.turborvip.security.domain.entity.UserDevice;
import io.jsonwebtoken.*;
import io.jsonwebtoken.SignatureException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.security.*;
import java.sql.Timestamp;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtService {
    @Autowired
    private TokenRepository tokenRepository;
    @Value("${dueTime.accessToken}")
    private Long dueTimeAccessToken;
    @Value("${dueTime.refreshToken}")
    private Long dueTimeRefreshToken;

    @Autowired
    private final TokenService tokenService;

    private final UserDeviceService userDeviceService;

    public String generateToken(User user, Collection<SimpleGrantedAuthority> authorities, String DEVICE_ID) throws NoSuchAlgorithmException {
        // TODO generate secret key
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        keyPairGen.initialize(4096);
        KeyPair pair = keyPairGen.generateKeyPair();
        PrivateKey privateKey = pair.getPrivate();
        PublicKey publicKey = pair.getPublic();
        String publicKeyString = Base64.getEncoder().encodeToString(publicKey.getEncoded());

        // TODO generate jwt
        Timestamp now = new Timestamp(System.currentTimeMillis());
        Timestamp expiredTime = new Timestamp(System.currentTimeMillis() + dueTimeAccessToken * 1000);
        String jwtGenerate = this.generateTokenUtil(user.getUsername(), authorities, privateKey, expiredTime);

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

        // TODO update or create token
        Token tokenExisted = tokenService.findFirstTokenByUserIdAndTypeAndDeviceId(user.getId(), "Bear", DEVICE_ID).orElse(null);
        if (tokenExisted != null) {
            // update value, publicKey, expiredTime, updateAt
            tokenService.updateTokenWithValueExpiredTime(tokenExisted, now, jwtGenerate, expiredTime, publicKeyString);
        } else {
            Token token = new Token(null, null, "Bear", jwtGenerate, publicKeyString, expiredTime, userDevice);
            token.setCreateBy(user);
            token.setUpdateBy(user);
            tokenService.create(token);
        }
        return jwtGenerate;
    }

    public String generateRefreshToken(User user, Collection<SimpleGrantedAuthority> authorities, String DEVICE_ID) throws NoSuchAlgorithmException {
        // TODO generate secret key
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        keyPairGen.initialize(4096);
        KeyPair pair = keyPairGen.generateKeyPair();
        PrivateKey privateKey = pair.getPrivate();
        PublicKey publicKey = pair.getPublic();
        String publicKeyString = Base64.getEncoder().encodeToString(publicKey.getEncoded());

        // TODO generate jwt
        Timestamp now = new Timestamp(System.currentTimeMillis());
        Timestamp expiredTime = new Timestamp(System.currentTimeMillis() + dueTimeRefreshToken * 1000);

        String jwtGenerate = this.generateTokenUtil(user.getUsername(), authorities, privateKey, expiredTime);

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

        // TODO check token
        Token tokenExisted = tokenService.findFirstTokenByUserIdAndTypeAndDeviceId(user.getId(), "Refresh", DEVICE_ID).orElse(null);
        if (tokenExisted != null) {
            // update value, publicKey, expiredTime, updateAt
            tokenService.updateTokenWithValueExpiredTime(tokenExisted, now, jwtGenerate, expiredTime, publicKeyString);
        } else {
            Token token = new Token(null, null, "Refresh", jwtGenerate, publicKeyString, expiredTime, userDevice);
            token.setCreateBy(user);
            token.setUpdateBy(user);
            tokenService.create(token);
        }
        return jwtGenerate;
    }

    public String generateTokenUtil(String username, Collection<SimpleGrantedAuthority> authorities, PrivateKey privateKey, Timestamp expiredTime) {
        Claims claims = Jwts.claims();
        List<String> roles = authorities.stream().map(GrantedAuthority::getAuthority).toList();
        claims.put("roles", roles);
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(expiredTime)
                .signWith(SignatureAlgorithm.RS256, privateKey)
                .setClaims(claims)
                .compact();
    }

    public boolean validationToken(String token, String publicKey) {
        try {
            Jwts.parser().setSigningKey(publicKey).parseClaimsJws(token);
            return true;
        } catch (SignatureException exception) {
            log.error("Invalid JWT signature :{}", exception.getMessage());
        } catch (MalformedJwtException exception) {
            log.error("Invalid JWT malformed :{}", exception.getMessage());
        } catch (ExpiredJwtException exception) {
            log.error("JWT token is expired :{}", exception.getMessage());
        } catch (UnsupportedJwtException exception) {
            log.error("JWT token is unsupported :{} ", exception.getMessage());
        } catch (IllegalArgumentException exception) {
            log.error("JWT claims is not empty :{}", exception.getMessage());
        }
        return false;
    }

    public void generateTokenFromRefreshToken(HttpServletResponse response, Token refreshToken, String deviceID) {
        try {
            List<Token> listToken = new ArrayList<>();
            Token refreshTokenDB = tokenService.findTokenByValueAndType(refreshToken.getValue(), refreshToken.getType())
                    .orElseThrow(() -> new Exception("Don't have anything refresh token"));
            if (refreshTokenDB != null) {
                Token accessToken = tokenService.findFirstAccessTokenByUserIdAndUserDevice(refreshTokenDB.getCreateBy().getId(), refreshToken.getUserDevice().getDeviceID())
                        .orElseThrow(() -> new Exception("Don't have any thing accessToken"));
                listToken.add(refreshTokenDB);
                listToken.add(accessToken);
                listToken.forEach(tokenRepository::delete);
            }
        } catch (Exception exception) {
            response.setHeader("error", exception.getMessage());
        }
    }
}
