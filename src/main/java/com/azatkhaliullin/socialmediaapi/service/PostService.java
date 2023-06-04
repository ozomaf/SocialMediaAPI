package com.azatkhaliullin.socialmediaapi.service;

import com.azatkhaliullin.socialmediaapi.Utils;
import com.azatkhaliullin.socialmediaapi.dto.Post;
import com.azatkhaliullin.socialmediaapi.dto.PostRequest;
import com.azatkhaliullin.socialmediaapi.dto.User;
import com.azatkhaliullin.socialmediaapi.repository.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

@Slf4j
@AllArgsConstructor
public class PostService {

    private final PostRepository postRepo;

    public Long createPost(PostRequest request) {
        User user = Utils.getCurrentAuthUser();
        Post post = Post.builder()
                .title(request.getTitle())
                .text(request.getText())
                .images(request.getImages())
                .author(user)
                .createdAt(LocalDateTime.now())
                .build();
        return postRepo.save(post).getId();
    }

    public void deletePost(Long postId) {
        getPost(postId);
        postRepo.deleteById(postId);
    }

    public ResponseEntity<String> updatePost(Long postId,
                                             PostRequest request) {
        User currentAuthUser = Utils.getCurrentAuthUser();
        Post post = getPost(postId);
        if (!currentAuthUser.equals(post.getAuthor())) {
            return ResponseEntity.badRequest().body("This post does not belong to this user.");
        }
        if (request.getTitle() != null) {
            post.setTitle(request.getTitle());
        }
        if (request.getText() != null) {
            post.setText(request.getText());
        }
        if (request.getImages() != null) {
            post.setImages(request.getImages());
        }
        postRepo.save(post);
        return ResponseEntity.ok("Post successfully updated");
    }

    private Post getPost(Long postId) {
        return postRepo.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found"));
    }

}