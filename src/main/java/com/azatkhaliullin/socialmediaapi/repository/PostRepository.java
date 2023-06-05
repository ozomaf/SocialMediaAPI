package com.azatkhaliullin.socialmediaapi.repository;

import com.azatkhaliullin.socialmediaapi.dto.Post;
import com.azatkhaliullin.socialmediaapi.dto.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PostRepository
        extends CrudRepository<Post, Long> {

    List<Post> getAllByAuthor(User author);

    @Query("select p from Post p " +
            "join Relationship r on (p.author = r.toWhom and r.who = ?1) " +
            "or (p.author = r.who and r.toWhom = ?1 and r.status = 0)" +
            "order by p.createdAt desc")
    List<Post> getAllPostsOfSubscribersDesc(User user, Pageable pageable);

}