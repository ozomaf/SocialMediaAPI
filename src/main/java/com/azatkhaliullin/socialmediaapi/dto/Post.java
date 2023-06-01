package com.azatkhaliullin.socialmediaapi.dto;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@NoArgsConstructor
public class Post {

    @Id
    @GeneratedValue
    private Long id;
    private String title;
    private String text;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Image> images;
    @NotNull
    private LocalDateTime createdAt;

    public Post(LocalDateTime createdAt) {
        this.createdAt = LocalDateTime.now();
    }

}