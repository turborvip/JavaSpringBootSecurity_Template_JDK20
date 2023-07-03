package com.turborvip.security.application.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.turborvip.security.application.services.JwtService;
import com.turborvip.security.application.services.TokenService;
import com.turborvip.security.domain.entity.Token;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Component
@RequiredArgsConstructor
@PropertySource({"classpath:application.properties"})
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Value("${jwt_secret}")
    private String SECRET_KEY;

    @Autowired
    private final TokenService tokenService;

    @Autowired
    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            try {
                // Todo find token in with value token in request
                String token = authorizationHeader.substring("Bearer " .length());
                Token tokenDB = tokenService.findFirstTokenByValue(token).orElseThrow(() -> new Exception("Don't find any token"));
                String publicKeyEncoded = tokenDB.getVerifyKey();
                PublicKey publicKey = generatePublicKey(publicKeyEncoded);
                if (jwtService.validationToken(tokenDB.getValue(), tokenDB.getVerifyKey())) {
                    System.out.println("a");

                    // back list
                }
                // Todo decode token using jwt
                Claims claims = Jwts.parser()
                        .setSigningKey(publicKey)
                        .parseClaimsJws(token)
                        .getBody();
                String username = claims.getSubject();
                List<String> roles = claims.get("roles", List.class);
                Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
                roles.forEach(role -> {
                    authorities.add(new SimpleGrantedAuthority(role));
                });

                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(username, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

                filterChain.doFilter(request, response);
            } catch (Exception exception) {
                response.setHeader("error", exception.getMessage());
                response.setStatus(FORBIDDEN.value());
                Map<String, String> error = new HashMap<>();
                error.put("error_message", exception.getMessage());
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), error);
            }
        } else {
            filterChain.doFilter(request, response);
        }
    }

    private static PublicKey generatePublicKey(String publicKeyBase64) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] publicKeyBytes = Base64.getDecoder().decode(publicKeyBase64);

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(new X509EncodedKeySpec(publicKeyBytes));
    }
}
