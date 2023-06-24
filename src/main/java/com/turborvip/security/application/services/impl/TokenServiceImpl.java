package com.turborvip.security.application.services.impl;

import com.turborvip.security.application.repositories.TokenRepository;
import com.turborvip.security.application.services.TokenService;
import com.turborvip.security.domain.entity.Token;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class TokenServiceImpl implements TokenService {
    @Autowired
    private final TokenRepository tokenRepository;

    @Override
    public Token create(Token token) {
        return tokenRepository.save(token);
    }
}
