package com.azatkhaliullin.socialmediaapi.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class Relationship {

    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne
    private User who;
    @ManyToOne
    private User toWhom;
    private Status status;
    private boolean isRequestDone;

    public enum Status {
        FRIEND, SUBSCRIBER
    }

}