package com.turborvip.security.application.configuration;

import com.turborvip.security.application.constants.EnumRole;
import com.turborvip.security.application.repositories.UserRepository;
import com.turborvip.security.application.services.UserService;
import com.turborvip.security.domain.entity.Role;
import com.turborvip.security.domain.entity.User;
import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class ApplicationConfig {
    private final UserRepository userRepository;

    @Bean
    public UserDetailsService userDetailsService(){
        return username -> userRepository.findByUsername(username).orElseThrow(()-> new UsernameNotFoundException("User not found"));
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception{
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    CommandLineRunner run(UserService userService, Environment environment) {
        return args -> {
            /*userService.saveRole(new Role(null, EnumRole.ROLE_USER));
            userService.saveRole(new Role(null, EnumRole.ROLE_SUPER_ADMIN));
            userService.saveRole(new Role(null, EnumRole.ROLE_ADMIN));
            userService.saveRole(new Role(null, EnumRole.MANAGER));
            //SimpleDateFormat formatter = new SimpleDateFormat(CommonConstant.FORMAT_DATE_PATTERN);
            Date now = new Date();

            HttpServletRequest request = null;
            User superAdmin = new User("TurborvipSuperAdmin", "turborvipSuperAdmin", "123456a", "turborvip@gmail.com", now, null, null, null, null, new HashSet<>());
            User user = new User("TurborvipUser", "turborvipUser", "123456a", "turborvip@gmail.com", now, null, null, null, null, new HashSet<>());
            User admin = new User("TurborvipAdmin", "turborvipAdmin", "123456a", "turborvip@gmail.com", now, null, null, null, null, new HashSet<>());
            User manager = new User("TurborvipManager", "turborvipManager", "123456a", "turborvip@gmail.com", now, null, null, null, null, new HashSet<>());

            userService.create(superAdmin, request);
            User superAdminCreated = userService.findById(superAdmin.getId()).orElse(null);

            user.setCreateBy(superAdminCreated);
            user.setUpdateBy(superAdminCreated);
            admin.setCreateBy(superAdminCreated);
            admin.setUpdateBy(superAdminCreated);
            manager.setCreateBy(superAdminCreated);
            manager.setUpdateBy(superAdminCreated);

            userService.create(user, request);
            userService.create(admin, request);
            userService.create(manager, request);

            userService.addToUser("turborvipSuperAdmin", String.valueOf(EnumRole.ROLE_SUPER_ADMIN));
            userService.addToUser("turborvipUser", String.valueOf(EnumRole.ROLE_USER));
            userService.addToUser("turborvipAdmin", String.valueOf(EnumRole.ROLE_ADMIN));
            userService.addToUser("turborvipManager", String.valueOf(EnumRole.MANAGER));*/
            //test
        };
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Collections.singletonList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
        configuration.setAllowedHeaders(Collections.singletonList("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public WebMvcConfigurer corsConfigure() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/v1/**")
                        .allowedOrigins("https://localhost:8077");
            }
        };
    }

    @Bean
    public TaskScheduler taskScheduler() {
        final ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(10);
        return scheduler;
    }
}
