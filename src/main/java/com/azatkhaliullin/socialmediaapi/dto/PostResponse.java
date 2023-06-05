package com.azatkhaliullin.socialmediaapi.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class PostResponse {

    private Long id;
    private String title;
    private String text;
    private List<Image> images;

}