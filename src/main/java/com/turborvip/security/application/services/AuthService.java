package com.turborvip.security.application.services;

import com.turborvip.security.application.request.AuthRequest;
import com.turborvip.security.application.response.AuthResponse;
import com.turborvip.security.application.constants.EnumRole;
import com.turborvip.security.application.repositories.RoleCusRepo;
import com.turborvip.security.application.repositories.UserRepository;
import com.turborvip.security.domain.entity.Role;
import com.turborvip.security.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final RoleCusRepo roleCusRepo;
    private final JwtService jwtService;

    public AuthResponse authenticate(AuthRequest authRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        User user = userRepository.findByUsername(authRequest.getUsername()).orElseThrow();
        List<Role> role = null;
        if (user != null) {
            role = roleCusRepo.findByUsers_Username(user.getUsername());
        }

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        Set<Role> set = new HashSet<>();
        role.stream().forEach(c -> set.add(new Role(EnumRole.valueOf(c.getRoleName().name()))));
        user.setRoles(set);
        set.stream().forEach(i -> authorities.add(new SimpleGrantedAuthority(i.getRoleName().toString())));

        var jwtToken = jwtService.generateToken(user, authorities);
        var jwtRefreshToken = jwtService.generateRefreshToken(user, authorities);
        return AuthResponse.builder().token(jwtToken).refreshToken(jwtRefreshToken).build();
    }
}
