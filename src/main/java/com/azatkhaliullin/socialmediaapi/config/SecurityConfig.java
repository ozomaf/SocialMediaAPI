package com.azatkhaliullin.socialmediaapi.config;

import com.azatkhaliullin.socialmediaapi.repository.UserRepository;
import com.azatkhaliullin.socialmediaapi.security.AuthEntryPoint;
import com.azatkhaliullin.socialmediaapi.security.AuthTokenFilter;
import com.azatkhaliullin.socialmediaapi.service.AuthService;
import com.azatkhaliullin.socialmediaapi.service.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthService authService(UserRepository userRepo,
                                   JwtService jwtService,
                                   PasswordEncoder encoder,
                                   AuthenticationManager authManager) {
        return new AuthService(userRepo, jwtService, encoder, authManager);
    }

    @Bean
    public AuthEntryPoint authEntryPoint(ObjectMapper objectMapper) {
        return new AuthEntryPoint(objectMapper);
    }

    @Bean
    public AuthTokenFilter authTokenFilter(JwtService jwtService,
                                           UserDetailsService userDetailsService) {
        return new AuthTokenFilter(jwtService, userDetailsService);
    }

    @Bean
    public AuthenticationManager authManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepo) {
        return username -> userRepo.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
                                           AuthEntryPoint authEntryPoint,
                                           AuthTokenFilter authTokenFilter) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "api/auth/**"
                        ).permitAll()
                        .anyRequest().authenticated())
                .exceptionHandling((exceptionHandling) -> exceptionHandling
                        .authenticationEntryPoint(authEntryPoint))
                .sessionManagement((sessionManagement) -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

}