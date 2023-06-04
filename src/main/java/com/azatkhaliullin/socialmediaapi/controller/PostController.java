package com.azatkhaliullin.socialmediaapi.controller;

import com.azatkhaliullin.socialmediaapi.dto.PostRequest;
import com.azatkhaliullin.socialmediaapi.service.PostService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/post")
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<Long> create(@RequestBody PostRequest postRequest) {
        return ResponseEntity.ok(postService.createPost(postRequest));
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<String> delete(@PathVariable Long postId) {
        postService.deletePost(postId);
        return ResponseEntity.ok("The post was successfully deleted");
    }

    @PutMapping("/{postId}")
    public ResponseEntity<String> update(@PathVariable Long postId,
                                         @RequestBody PostRequest postRequest) {
        return postService.updatePost(postId, postRequest);
    }

}