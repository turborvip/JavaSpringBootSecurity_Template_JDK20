package com.turborvip.security.application.services.impl;

import com.turborvip.security.application.repositories.TokenRepository;
import com.turborvip.security.application.services.TokenService;
import com.turborvip.security.domain.entity.Token;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class TokenServiceImpl implements TokenService {
    @Autowired
    private final TokenRepository tokenRepository;

    @Override
    public Token create(Token token) {
        return tokenRepository.save(token);
    }

    @Override
    public Optional<Token> findFirstTokenByUserIdAndTypeAndDeviceId(Long userId, String type, String DeviceId) {
        return tokenRepository.findFirstByCreateBy_IdAndTypeAndUserDevice_DeviceID(userId, type, DeviceId);
    }

    @Override
    public void delete(Long id) {
        tokenRepository.deleteById(id);
    }

    @Override
    public Token updateTokenWithValueExpiredTime(Token tokenOld, Timestamp updateAt, String value, Timestamp expiredAt, String verifyKey) {
        tokenOld.setUpdateAt(updateAt);
        tokenOld.setValue(value);
        tokenOld.setExpiresAt(expiredAt);
        tokenOld.setVerifyKey(verifyKey);
        return tokenRepository.save(tokenOld);
    }

    @Override
    public List<Token> findListTokenByUserAndDevice(Long userId, String deviceId) {
        return tokenRepository.findByCreateBy_IdAndUserDevice_DeviceID(userId, deviceId);
    }

    @Override
    public Optional<Token> findFirstTokenByValue(String value) {
        return tokenRepository.findFirstByValue(value);
    }

    @Override
    public Optional<Token> findTokenByValueAndType(String tokenValue, String type) {
        return tokenRepository.findFirstByValueAndType(tokenValue,type);
    }

    @Override
    public List<Token> findListTokenExpired() {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        return tokenRepository.findByExpiresAtLessThan(now);
    }

    @Override
    public Optional<Token> findFirstAccessTokenByUserIdAndUserDevice(Long userId, String userDevice) {
        return tokenRepository.findFirstByCreateBy_IdAndTypeAndUserDevice_DeviceID(userId,"Bear",userDevice);
    }
}
