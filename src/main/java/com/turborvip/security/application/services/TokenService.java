package com.turborvip.security.application.services;

import com.turborvip.security.domain.entity.Token;
import jakarta.servlet.http.HttpServletRequest;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public interface TokenService {
    Token create(Token token);

    Optional<Token> findFirstTokenByUserIdAndTypeAndDeviceId(Long userId, String type, String DeviceId);

    void delete(Long tokenId);

    int updateTokenWithValueExpiredTime(Long tokenId,Timestamp updateAt, String value, Timestamp expiredAt);

    List<Token> findListTokenByUserAndDevice(Long userId,String deviceId);

    Optional<Token> findFirstTokenByValue(String value);

    List<Token> findListTokenExpired();

}
