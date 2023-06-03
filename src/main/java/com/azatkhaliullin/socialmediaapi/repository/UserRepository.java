package com.azatkhaliullin.socialmediaapi.repository;

import com.azatkhaliullin.socialmediaapi.dto.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository
        extends CrudRepository<User, Long> {

    Optional<User> findByUsername(String username);

}