package com.azatkhaliullin.socialmediaapi.repository;

import com.azatkhaliullin.socialmediaapi.dto.Relationship;
import com.azatkhaliullin.socialmediaapi.dto.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RelationshipRepository
        extends CrudRepository<Relationship, Long> {

    boolean existsByWhoAndToWhom(User who, User toWhom);

    Optional<Relationship> findByWhoAndToWhom(User who, User toWhom);

    Optional<Relationship> findByWhoAndToWhomAndStatus(User who, User toWhom, Relationship.Status status);

}