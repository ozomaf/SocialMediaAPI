package com.azatkhaliullin.socialmediaapi.config;

import com.azatkhaliullin.socialmediaapi.Exception.PostValidationException;
import com.azatkhaliullin.socialmediaapi.dto.Post;
import com.azatkhaliullin.socialmediaapi.repository.PostRepository;
import com.azatkhaliullin.socialmediaapi.repository.UserRepository;
import com.azatkhaliullin.socialmediaapi.service.JwtService;
import com.azatkhaliullin.socialmediaapi.service.PostService;
import com.azatkhaliullin.socialmediaapi.service.UserService;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

@Aspect
@Configuration
public class AppConfig {

    @Bean
    public JwtService jwtService() {
        return new JwtService();
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public PostService postService(PostRepository postRepo,
                                   UserService userService,
                                   ModelMapper modelMapper) {
        return new PostService(postRepo, userService, modelMapper);
    }

    @Bean
    public UserService userService(UserRepository userRepo,
                                   JwtService jwtService,
                                   PasswordEncoder encoder,
                                   AuthenticationManager authManager) {
        return new UserService(userRepo, jwtService, encoder, authManager);
    }

    @Before("execution(* com.azatkhaliullin.socialmediaapi.repository.PostRepository.save(..)) && args(entity)")
    public void validatePostBeforeSave(Post entity) {
        if ((entity.getTitle() == null || entity.getTitle().isBlank())
                && (entity.getText() == null || entity.getText().isBlank())
                && (entity.getImages() == null || entity.getImages().isEmpty())) {
            throw new PostValidationException();
        }
    }

}