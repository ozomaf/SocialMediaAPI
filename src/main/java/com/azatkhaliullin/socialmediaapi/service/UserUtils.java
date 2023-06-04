package com.azatkhaliullin.socialmediaapi.service;

import com.azatkhaliullin.socialmediaapi.dto.User;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.context.SecurityContextHolder;

@UtilityClass
public class UserUtils {

    public User getCurrentAuthUser() {
        return (User) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
    }

}