package com.azatkhaliullin.socialmediaapi.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.Map;

@AllArgsConstructor
public class AuthEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        String errorMessage;
        if (authException instanceof InsufficientAuthenticationException) {
            errorMessage = "Invalid tokens";
        } else {
            errorMessage = "Unknown authentication error";
        }
        errorMessage += ": " + authException.getMessage();
        Map<String, String> errorResponse = Collections.singletonMap("error", errorMessage);
        try (PrintWriter writer = response.getWriter()) {
            writer.write(objectMapper.writeValueAsString(errorResponse));
        }
    }

}