package com.azatkhaliullin.socialmediaapi.config;

import com.azatkhaliullin.socialmediaapi.dto.Post;
import com.azatkhaliullin.socialmediaapi.repository.PostRepository;
import com.azatkhaliullin.socialmediaapi.service.JwtService;
import com.azatkhaliullin.socialmediaapi.service.PostService;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Aspect
@Configuration
public class AppConfig {

    @Bean
    public JwtService jwtService() {
        return new JwtService();
    }

    @Bean
    public PostService postService(PostRepository postRepo) {
        return new PostService(postRepo);
    }

    @Before("execution(* com.azatkhaliullin.socialmediaapi.repository.PostRepository.save(..)) && args(entity)")
    public void validatePostBeforeSave(Post entity) {
        if ((entity.getTitle() == null || entity.getTitle().isBlank())
                && (entity.getText() == null || entity.getText().isBlank())
                && (entity.getImages() == null || entity.getImages().isEmpty())) {
            throw new IllegalArgumentException("Post must not be null or empty");
        }
    }

}