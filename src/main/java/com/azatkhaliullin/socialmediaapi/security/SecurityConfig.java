package com.azatkhaliullin.socialmediaapi.security;

import com.azatkhaliullin.socialmediaapi.repository.UserRepository;
import com.azatkhaliullin.socialmediaapi.service.JwtUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public JwtUtils jwtUtils() {
        return new JwtUtils();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthEntryPoint authEntryPoint(ObjectMapper objectMapper) {
        return new AuthEntryPoint(objectMapper);
    }

    @Bean
    public AuthTokenFilter authTokenFilter(JwtUtils jwtUtils,
                                           UserDetailsService userDetailsService) {
        return new AuthTokenFilter(jwtUtils, userDetailsService);
    }

    @Bean
    public AuthenticationManager authManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepo) {
        return username -> userRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
                                           AuthEntryPoint authEntryPoint,
                                           AuthTokenFilter authTokenFilter) throws Exception {
        http.authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "api/auth/**").permitAll()
                        .anyRequest().authenticated())
                .addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling((exceptionHandling) -> exceptionHandling
                        .authenticationEntryPoint(authEntryPoint))
                .sessionManagement((sessionManagement) -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();
    }

}