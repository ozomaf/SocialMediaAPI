package com.azatkhaliullin.socialmediaapi.repository;

import com.azatkhaliullin.socialmediaapi.dto.Post;
import org.springframework.data.repository.CrudRepository;

public interface PostRepository
        extends CrudRepository<Post, Long> {
}