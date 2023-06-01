package com.azatkhaliullin.socialmediaapi.repository;

import com.azatkhaliullin.socialmediaapi.dto.Image;
import org.springframework.data.repository.CrudRepository;

public interface ImageRepository
        extends CrudRepository<Image, Long> {
}