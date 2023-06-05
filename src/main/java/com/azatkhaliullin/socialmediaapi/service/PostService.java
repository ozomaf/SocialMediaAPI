package com.azatkhaliullin.socialmediaapi.service;

import com.azatkhaliullin.socialmediaapi.Exception.InvalidPostOwnershipException;
import com.azatkhaliullin.socialmediaapi.dto.Post;
import com.azatkhaliullin.socialmediaapi.dto.PostRequest;
import com.azatkhaliullin.socialmediaapi.dto.PostResponse;
import com.azatkhaliullin.socialmediaapi.dto.User;
import com.azatkhaliullin.socialmediaapi.repository.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@AllArgsConstructor
public class PostService {

    private final PostRepository postRepo;
    private final UserService userService;
    private final ModelMapper modelMapper;

    public PostResponse createPost(PostRequest request) {
        User user = userService.getCurrentAuthUser();
        Post post = Post.builder()
                .title(request.getTitle())
                .text(request.getText())
                .images(request.getImages())
                .author(user)
                .createdAt(LocalDateTime.now())
                .build();
        Post save = postRepo.save(post);
        return modelMapper.map(save, PostResponse.class);
    }

    public void deletePost(Long postId) {
        if (checkOwner(getPost(postId))) {
            getPost(postId);
            postRepo.deleteById(postId);
        }
    }

    public PostResponse updatePost(Long postId,
                                   PostRequest request) {
        Post post = getPost(postId);
        if (checkOwner(post)) {
            if (request.getTitle() != null) {
                post.setTitle(request.getTitle());
            }
            if (request.getText() != null) {
                post.setText(request.getText());
            }
            if (request.getImages() != null) {
                post.setImages(request.getImages());
            }
        }
        Post save = postRepo.save(post);
        return modelMapper.map(save, PostResponse.class);
    }

    private Post getPost(Long postId) {
        return postRepo.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found"));
    }

    public List<PostResponse> getAllPostsByUser(String username) {
        User user = userService.getUser(username);
        List<Post> posts = postRepo.getAllByAuthor(user);
        List<PostResponse> postResponses = new ArrayList<>();
        for (Post post : posts) {
            postResponses.add(modelMapper.map(post, PostResponse.class));
        }
        return postResponses;
    }

    private boolean checkOwner(Post post) {
        User currentAuthUser = userService.getCurrentAuthUser();
        if (currentAuthUser.equals(post.getAuthor())) {
            return true;
        } else {
            throw new InvalidPostOwnershipException();
        }
    }

}