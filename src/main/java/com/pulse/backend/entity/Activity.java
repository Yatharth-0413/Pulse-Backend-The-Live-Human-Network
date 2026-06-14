package com.pulse.backend.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "activities")
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String type;

    private String description;

    private Integer points;

    private LocalDateTime createdAt;

    public Activity() {
    }

    public Activity(
            String username,
            String type,
            String description,
            Integer points
    ) {
        this.username = username;
        this.type = type;
        this.description = description;
        this.points = points;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public Integer getPoints() {
        return points;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}