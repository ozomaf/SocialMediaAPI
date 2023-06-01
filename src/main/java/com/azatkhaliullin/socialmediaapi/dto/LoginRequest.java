package com.azatkhaliullin.socialmediaapi.dto;

import lombok.Data;

@Data
public class LoginRequest {

    private String username;
    private String email;

}