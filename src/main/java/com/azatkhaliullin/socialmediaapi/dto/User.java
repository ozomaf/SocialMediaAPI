package com.azatkhaliullin.socialmediaapi.dto;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Set;

@Entity
public class User {

    @Id
    @GeneratedValue
    private Long id;
    @NotNull
    @Column(unique = true)
    private String username;
    @NotNull
    @Column(unique = true)
    private String email;
    @NotNull
    private String password;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Post> posts;
    @ManyToMany(cascade = CascadeType.ALL)
    private Set<User> friends;
    @ManyToMany(cascade = CascadeType.ALL)
    private Set<User> subscribers;
    @ManyToMany
    private Set<User> friendshipRequestsSent;

}