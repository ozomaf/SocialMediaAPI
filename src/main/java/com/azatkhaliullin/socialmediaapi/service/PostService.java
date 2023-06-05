package com.azatkhaliullin.socialmediaapi.service;

import com.azatkhaliullin.socialmediaapi.Exception.InvalidPostOwnershipException;
import com.azatkhaliullin.socialmediaapi.dto.Post;
import com.azatkhaliullin.socialmediaapi.dto.User;
import com.azatkhaliullin.socialmediaapi.repository.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@AllArgsConstructor
public class PostService {

    private final UserService userService;
    private final PostRepository postRepo;

    public Post createPost(Post post,
                           User user) {
        Post build = Post.builder()
                .title(post.getTitle())
                .text(post.getText())
                .images(post.getImages())
                .author(user)
                .createdAt(LocalDateTime.now())
                .build();
        return postRepo.save(build);
    }

    public void deletePost(Long postId,
                           User user) {
        Post post = postRepo.findById(postId)
                .orElseThrow(EntityNotFoundException::new);
        checkOwner(post, user);
        postRepo.deleteById(postId);
    }

    public Post updatePost(Post post,
                           User user) {
        Post existingPost = postRepo.findById(post.getId())
                .orElseThrow(EntityNotFoundException::new);
        checkOwner(existingPost, user);
        if (post.getTitle() != null) {
            existingPost.setTitle(post.getTitle());
        }
        if (post.getText() != null) {
            existingPost.setText(post.getText());
        }
        if (post.getImages() != null) {
            existingPost.setImages(post.getImages());
        }
        return postRepo.save(existingPost);
    }

    public List<Post> getAllPostsByUser(Long userId) {
        User user = userService.getUserById(userId);
        return postRepo.getAllByAuthor(user);
    }

    public List<Post> getAllPostsOfSubscriptions(User user,
                                                 Pageable pageable) {
        return postRepo.getAllPostsOfSubscribersDesc(user, pageable);
    }

    private void checkOwner(Post post,
                            User user) {
        if (!user.equals(post.getAuthor())) {
            throw new InvalidPostOwnershipException();
        }
    }

}