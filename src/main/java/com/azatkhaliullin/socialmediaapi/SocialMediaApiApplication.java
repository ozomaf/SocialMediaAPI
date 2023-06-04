package com.azatkhaliullin.socialmediaapi;

import com.azatkhaliullin.socialmediaapi.repository.PostRepository;
import com.azatkhaliullin.socialmediaapi.service.JwtUtils;
import com.azatkhaliullin.socialmediaapi.service.PostUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SocialMediaApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(SocialMediaApiApplication.class, args);
    }

    @Bean
    public JwtUtils jwtUtils() {
        return new JwtUtils();
    }

    @Bean
    public PostUtils postUtils(PostRepository postRepo) {
        return new PostUtils(postRepo);
    }

}