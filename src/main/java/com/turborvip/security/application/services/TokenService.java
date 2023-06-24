package com.turborvip.security.application.services;

import com.turborvip.security.domain.entity.Token;
import jakarta.servlet.http.HttpServletRequest;

public interface TokenService {
    Token create(Token token);
}
