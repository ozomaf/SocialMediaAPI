package com.azatkhaliullin.socialmediaapi.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class Image {

    @Id
    @GeneratedValue
    private Long id;
    @Lob
    private byte[] data;

    public Image(byte[] data) {
        this.data = data;
    }

}