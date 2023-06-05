package com.azatkhaliullin.socialmediaapi.service;

import com.azatkhaliullin.socialmediaapi.Exception.AccountExistsException;
import com.azatkhaliullin.socialmediaapi.FieldValidationUtils;
import com.azatkhaliullin.socialmediaapi.dto.AuthRequest;
import com.azatkhaliullin.socialmediaapi.dto.User;
import com.azatkhaliullin.socialmediaapi.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

@AllArgsConstructor
public class UserService {

    private final UserRepository userRepo;
    private final JwtService jwtService;
    private final PasswordEncoder encoder;
    private final AuthenticationManager authManager;

    public String signUp(AuthRequest request) {
        FieldValidationUtils.validateSignUpRequest(request);
        if (userRepo.existsByUsername(request.getUsername()) ||
                userRepo.existsByEmail(request.getEmail())) {
            throw new AccountExistsException();
        }
        User user = userRepo.save(User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(encoder.encode(request.getPassword()))
                .role(User.UserRole.USER)
                .build());
        return jwtService.generateToken(user);
    }

    public String login(AuthRequest request) {
        FieldValidationUtils.validateLoginRequest(request);
        Authentication authenticate = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        User user = (User) authenticate.getPrincipal();
        return jwtService.generateToken(user);
    }

    public User getCurrentAuthUser() {
        return (User) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
    }

    public User getUser(String username) {
        return userRepo.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

}