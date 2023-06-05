package com.azatkhaliullin.socialmediaapi.controller;

import com.azatkhaliullin.socialmediaapi.dto.User;
import com.azatkhaliullin.socialmediaapi.service.AuthService;
import com.azatkhaliullin.socialmediaapi.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.naming.OperationNotSupportedException;

@RestController
@AllArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    private final AuthService authService;

    @PostMapping("/subscribe/{targetUserId}")
    public void subscribeTo(@PathVariable Long targetUserId) {
        User user = authService.getCurrentAuthUser();
        userService.subscribeTo(user, targetUserId);
    }

    @DeleteMapping("/subscribe/{targetUserId}")
    public void unsubscribeFrom(@PathVariable Long targetUserId) {
        User user = authService.getCurrentAuthUser();
        userService.unsubscribeFrom(user, targetUserId);
    }

    @PutMapping("/subscribe/{targetUserId}")
    public void processSubscribeRequest(@PathVariable Long targetUserId,
                                        @RequestParam boolean isAccepted) {
        User user = authService.getCurrentAuthUser();
        userService.processSubscribeRequest(user, targetUserId, isAccepted);
    }

    @PostMapping("/chat/{targetUserId}")
    public void sendMessage(@PathVariable Long targetUserId,
                            @RequestBody String text) throws OperationNotSupportedException {
        User user = authService.getCurrentAuthUser();
        userService.sendMessage(user, targetUserId, text);
    }

}