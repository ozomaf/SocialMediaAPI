package com.azatkhaliullin.socialmediaapi.controller;

import com.azatkhaliullin.socialmediaapi.dto.AuthRequest;
import com.azatkhaliullin.socialmediaapi.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    @PostMapping("/signup")
    public String signUp(@RequestBody AuthRequest request) {
        return userService.signUp(request);
    }

    @PostMapping("/login")
    public String login(@RequestBody AuthRequest request) {
        return userService.login(request);
    }

}