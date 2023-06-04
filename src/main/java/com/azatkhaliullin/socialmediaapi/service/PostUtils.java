package com.azatkhaliullin.socialmediaapi.service;

import com.azatkhaliullin.socialmediaapi.dto.Image;
import com.azatkhaliullin.socialmediaapi.dto.Post;
import com.azatkhaliullin.socialmediaapi.dto.User;
import com.azatkhaliullin.socialmediaapi.repository.PostRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@AllArgsConstructor
public class PostUtils {

    private final PostRepository postRepo;

    public Long create(String title,
                       String text,
                       List<MultipartFile> images) {
        List<Image> imageList = new ArrayList<>();
        User user = UserUtils.getCurrentAuthUser();
        try {
            for (MultipartFile image : images) {
                byte[] bytes = image.getBytes();
                imageList.add(new Image(bytes));
            }
            Post post = Post.builder()
                    .title(title)
                    .text(text)
                    .images(imageList)
                    .author(user)
                    .createdAt(LocalDateTime.now())
                    .build();
            return postRepo.save(post).getId();
        } catch (Exception e) {
            log.error("Error in post creation", e);
            throw new RuntimeException(e);
        }
    }

}