package com.turborvip.security.application.services;

import com.turborvip.security.adapter.web.base.VsResponseUtil;
import com.turborvip.security.application.configuration.exception.ForbiddenException;
import com.turborvip.security.application.repositories.RoleCusRepo;
import com.turborvip.security.application.repositories.TokenRepository;
import com.turborvip.security.application.response.AuthResponse;
import com.turborvip.security.domain.entity.Token;
import com.turborvip.security.domain.entity.User;
import com.turborvip.security.domain.entity.UserDevice;
import io.jsonwebtoken.*;
import io.jsonwebtoken.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.*;
import java.sql.Timestamp;
import java.util.*;

import static com.turborvip.security.application.constants.DevMessageConstant.Common.REFRESH_TOKEN_FAIL;
import static com.turborvip.security.application.constants.DevMessageConstant.Common.REFRESH_TOKEN_SUCCESS;


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

    @Autowired
    private final RoleCusRepo roleCusRepo;

    private final UserDeviceService userDeviceService;

    public String generateToken(User user, List<String> roles, String DEVICE_ID) throws NoSuchAlgorithmException {
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
        String jwtGenerate = this.generateTokenUtil(user.getUsername(), roles, privateKey, expiredTime);

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
        Token tokenExisted = tokenService.findFirstTokenByUserIdAndTypeAndDeviceId(user.getId(), "Access", DEVICE_ID).orElse(null);
        if (tokenExisted != null) {
            // update value, publicKey, expiredTime, updateAt
            tokenService.updateTokenWithValueExpiredTime(tokenExisted, now, jwtGenerate, expiredTime, publicKeyString);
        } else {
            Token token = new Token(null, null, "Access", jwtGenerate, publicKeyString, new ArrayList<>(), expiredTime, userDevice);
            token.setCreateBy(user);
            token.setUpdateBy(user);
            tokenService.create(token);
        }
        return jwtGenerate;
    }

    public String generateRefreshToken(User user, List<String> roles, String DEVICE_ID) throws NoSuchAlgorithmException {
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

        String jwtGenerate = this.generateTokenUtil(user.getUsername(), roles, privateKey, expiredTime);

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
            Token token = new Token(null, null, "Refresh", jwtGenerate, publicKeyString, new ArrayList<>(), expiredTime, userDevice);
            token.setCreateBy(user);
            token.setUpdateBy(user);
            tokenService.create(token);
        }
        return jwtGenerate;
    }

    public String generateTokenUtil(String username, List<String> roles, PrivateKey privateKey, Timestamp expiredTime) {
        Claims claims = Jwts.claims();
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

    public void generateTokenFromRefreshToken(String refreshToken, List<String> roles, String DEVICE_ID) {
        /*
            1. Check refresh token used in dbs
            2. Check refresh token in dbs
            3. Create token and refreshToken
            4. Update
         */

        try {
            // 1. Abnormal : bat thuong
            Token refreshTokenUsed = tokenService.findByRefreshTokenUsed(refreshToken).orElse(null);
            System.out.println(refreshTokenUsed);
            if (refreshTokenUsed != null) {
                // Todo remove all token of userId
                List<Token> listByUser = tokenService.findByUserId(refreshTokenUsed.getCreateBy().getId());
                listByUser.forEach(tokenRepository::delete);

                // back list
                // send mail
                throw new ForbiddenException("Some thing wrong happened ! Please re login ! ");
            }

            // 2.
            Token refreshTokenDB = tokenService.findTokenByValueAndType(refreshToken, "Refresh")
                    .orElseThrow(() -> new Exception("Don't have anything refresh token"));

            // 3. vs 4
            // create token
            String jwtToken = this.generateToken(refreshTokenDB.getCreateBy(), roles, DEVICE_ID);

            //create refresh token
            String jwtRefreshToken =  this.generateRefreshToken(refreshTokenDB.getCreateBy(), roles, DEVICE_ID);

            var data = AuthResponse.builder().token(jwtToken).refreshToken(jwtRefreshToken).build();

            VsResponseUtil.ok(REFRESH_TOKEN_SUCCESS, data);

        } catch (Exception exception) {
            VsResponseUtil.ok(REFRESH_TOKEN_FAIL, null);
        }
    }


}
