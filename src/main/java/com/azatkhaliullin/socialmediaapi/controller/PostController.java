package com.azatkhaliullin.socialmediaapi.controller;

import com.azatkhaliullin.socialmediaapi.dto.Post;
import com.azatkhaliullin.socialmediaapi.dto.PostData;
import com.azatkhaliullin.socialmediaapi.dto.User;
import com.azatkhaliullin.socialmediaapi.service.AuthService;
import com.azatkhaliullin.socialmediaapi.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping("/api/post")
public class PostController {

    private final PostService postService;
    private final AuthService authService;
    private final ModelMapper modelMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new post")
    public PostData create(@RequestBody PostData request) {
        Post post = modelMapper.map(request, Post.class);
        User user = authService.getCurrentAuthUser();
        Post createdPost = postService.createPost(post, user);
        return modelMapper.map(createdPost, PostData.class);
    }

    @DeleteMapping("/{postId}")
    @Operation(summary = "Delete a post by ID")
    public void delete(@PathVariable Long postId) {
        User user = authService.getCurrentAuthUser();
        postService.deletePost(postId, user);
    }

    @PutMapping
    @Operation(summary = "Update a post")
    public PostData update(@RequestBody PostData request) {
        Post post = modelMapper.map(request, Post.class);
        User user = authService.getCurrentAuthUser();
        Post updatedPost = postService.updatePost(post, user);
        return modelMapper.map(updatedPost, PostData.class);
    }

    @GetMapping("/{userId}")
    @Operation(summary = "Get all posts by user ID")
    public List<PostData> getAllPostsByUser(@PathVariable Long userId) {
        List<Post> allPostsByUser = postService.getAllPostsByUser(userId);
        return allPostsByUser.stream()
                .map(post -> modelMapper.map(post, PostData.class))
                .collect(Collectors.toList());
    }

    @GetMapping("/subscriptions")
    @Operation(summary = "Get all posts of subscriptions")
    public List<PostData> getAllPostsOfSubscriptions(@RequestParam Integer pageNumber,
                                                     @RequestParam Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        User user = authService.getCurrentAuthUser();
        List<Post> allPostsOfSubscriptions = postService.getAllPostsOfSubscriptions(user, pageable);
        return allPostsOfSubscriptions.stream()
                .map(post -> modelMapper.map(post, PostData.class))
                .collect(Collectors.toList());
    }

}