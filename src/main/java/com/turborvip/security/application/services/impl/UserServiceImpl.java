package com.turborvip.security.application.services.impl;

import com.turborvip.security.application.constants.EnumRole;
import com.turborvip.security.application.repositories.RoleRepository;
import com.turborvip.security.application.repositories.UserRepository;
import com.turborvip.security.application.services.UserService;
import com.turborvip.security.domain.entity.Role;
import com.turborvip.security.domain.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private final RoleRepository roleRepository;

    @Autowired
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;


    @Override
    public User create(User user, HttpServletRequest request) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Role saveRole(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public void addToUser(String username, String role_name) {
        User user = userRepository.findByUsername(username).get();
        Role role = roleRepository.findRoleByRoleName(EnumRole.valueOf(role_name));

        user.getRoles().add(role);
    }
}
