package com.azatkhaliullin.socialmediaapi;

import com.azatkhaliullin.socialmediaapi.dto.User;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.context.SecurityContextHolder;

@UtilityClass
public class Utils {

    public User getCurrentAuthUser() {
        return (User) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
    }

}