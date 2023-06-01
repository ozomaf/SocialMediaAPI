package com.azatkhaliullin.socialmediaapi.repository;

import com.azatkhaliullin.socialmediaapi.dto.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository
        extends CrudRepository<User, Long> {
}