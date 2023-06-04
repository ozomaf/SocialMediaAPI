package com.azatkhaliullin.socialmediaapi.controller;

import com.azatkhaliullin.socialmediaapi.dto.User;
import com.azatkhaliullin.socialmediaapi.repository.UserRepository;
import com.azatkhaliullin.socialmediaapi.service.JwtService;
import lombok.AllArgsConstructor;
import org.postgresql.util.PSQLException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final JwtService jwtService;
    private final UserRepository userRepo;
    private final PasswordEncoder encoder;
    private final AuthenticationManager authManager;

    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestParam String username,
                                         @RequestParam String email,
                                         @RequestParam String password) {
        User user = userRepo.save(User.builder()
                .username(username)
                .email(email)
                .password(encoder.encode(password))
                .build());
        String token = jwtService.generateToken(user);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String username,
                                        @RequestParam String password) {
        Authentication authenticate = authManager
                .authenticate(new UsernamePasswordAuthenticationToken(username, password));
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        User user = (User) authenticate.getPrincipal();
        String token = jwtService.generateToken(user);
        return ResponseEntity.ok(token);
    }

}