package com.azatkhaliullin.socialmediaapi.controller;

import com.azatkhaliullin.socialmediaapi.dto.AuthData;
import com.azatkhaliullin.socialmediaapi.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public String signUp(@RequestBody AuthData request) {
        return authService.signUp(request);
    }

    @PostMapping("/login")
    public String login(@RequestBody AuthData request) {
        return authService.login(request);
    }

}