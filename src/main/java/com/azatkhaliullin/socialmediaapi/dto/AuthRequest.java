package com.azatkhaliullin.socialmediaapi.dto;

import lombok.Data;

@Data
public class AuthRequest {

    private String username;
    private String email;
    private String password;

}