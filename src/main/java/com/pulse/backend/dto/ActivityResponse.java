package com.pulse.backend.dto;

import java.time.LocalDateTime;

public class ActivityResponse {

    private String description;

    private int points;

    private LocalDateTime createdAt;

    public ActivityResponse(
            String description,
            int points,
            LocalDateTime createdAt
    ) {
        this.description = description;
        this.points = points;
        this.createdAt = createdAt;
    }

    public String getDescription() {
        return description;
    }

    public int getPoints() {
        return points;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}