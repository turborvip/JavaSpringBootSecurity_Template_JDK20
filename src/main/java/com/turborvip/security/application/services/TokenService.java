package com.turborvip.security.application.services;

import com.turborvip.security.domain.entity.Token;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public interface TokenService {
    Token create(Token token);

    Optional<Token> findFirstTokenByUserIdAndTypeAndDeviceId(Long userId, String type, String DeviceId);

    void delete(Long tokenId);

//    int updateTokenWithValueExpiredTime(Long tokenId,Timestamp updateAt, String value, Timestamp expiredAt);

    Token updateTokenWithValueExpiredTime(Token tokenOld,Timestamp updateAt, String value, Timestamp expiredAt,String verifyKey);

    List<Token> findListTokenByUserAndDevice(Long userId,String deviceId);

    Optional<Token> findFirstTokenByValue(String value);

    Optional<Token> findTokenByValueAndType(String tokenValue, String type);

    List<Token> findListTokenExpired();

    Optional<Token> findFirstAccessTokenByUserIdAndUserDevice(Long userId, String userDevice);

    Optional<Token> findByRefreshTokenUsed(String refreshToken);

    List<Token> findByUserId(Long userId);

}
