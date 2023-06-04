package com.azatkhaliullin.socialmediaapi.controller;

import com.azatkhaliullin.socialmediaapi.service.PostUtils;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/post")
public class PostController {

    private final PostUtils postUtils;

    @PostMapping
    public ResponseEntity<Long> create(@RequestParam String title,
                                       @RequestParam String text,
                                       @RequestParam List<MultipartFile> images) {
        return ResponseEntity.ok(postUtils.create(title, text, images));
    }

}