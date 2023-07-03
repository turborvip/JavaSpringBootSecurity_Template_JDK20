package com.turborvip.security.application.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.turborvip.security.application.repositories.TokenRepository;
import com.turborvip.security.application.request.AuthRequest;
import com.turborvip.security.application.response.AuthResponse;
import com.turborvip.security.application.constants.EnumRole;
import com.turborvip.security.application.repositories.RoleCusRepo;
import com.turborvip.security.application.repositories.UserRepository;
import com.turborvip.security.domain.entity.Role;
import com.turborvip.security.domain.entity.Token;
import com.turborvip.security.domain.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpHeaders.USER_AGENT;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final RoleCusRepo roleCusRepo;
    private final JwtService jwtService;
    private final TokenService tokenService;
    private final TokenRepository tokenRepository;

    public AuthResponse authenticate(AuthRequest authRequest, HttpServletRequest request) throws NoSuchAlgorithmException {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        User user = userRepository.findByUsername(authRequest.getUsername()).orElse(null);
        List<Role> role = null;
        if (user != null) {
            role = roleCusRepo.findByUsers_Username(user.getUsername());
        }

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        Set<Role> set = new HashSet<>();
        role.stream().forEach(c -> set.add(new Role(EnumRole.valueOf(c.getRoleName().name()))));
        user.setRoles(set);
        set.stream().forEach(i -> authorities.add(new SimpleGrantedAuthority(i.getRoleName().toString())));

        String DEVICE_ID = request.getHeader(USER_AGENT);

        var jwtToken = jwtService.generateToken(user, authorities, DEVICE_ID);
        var jwtRefreshToken = jwtService.generateRefreshToken(user, authorities, DEVICE_ID);

        return AuthResponse.builder().token(jwtToken).refreshToken(jwtRefreshToken).build();
    }

    public void removeToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String DEVICE_ID = request.getHeader(USER_AGENT);
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if (authorizationHeader!=null && authorizationHeader.startsWith("Bearer ")){
            try {
                String token = authorizationHeader.substring("Bearer ".length());
                Token tokenExist = tokenService.findFirstTokenByValue(token).orElse(null);
                if (tokenExist != null){
                    List<Token> listToken = tokenService.findListTokenByUserAndDevice(tokenExist.getCreateBy().getId(),DEVICE_ID);
                    listToken.forEach(tokenRepository::delete);
                }
            }catch(Exception exception){
                response.setHeader("error",exception.getMessage());
                response.setStatus(FORBIDDEN.value());
                Map<String,String> error = new HashMap<>();
                error.put("error_message",exception.getMessage());
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(),error);
            }
        }
    }
}
