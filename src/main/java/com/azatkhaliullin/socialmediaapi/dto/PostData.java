package com.azatkhaliullin.socialmediaapi.dto;

import lombok.Data;

import java.util.List;

@Data
public class PostData {

    private Long id;
    private String title;
    private String text;
    private List<Image> images;

}