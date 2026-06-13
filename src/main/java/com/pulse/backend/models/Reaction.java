package com.pulse.backend.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "reactions")
public class Reaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long messageId;
    private String username;
    private String reactionType;
    private LocalDateTime createdAt;

    public Reaction() {
    }

    public Reaction(Long messageId, String username, String reactionType) {
        this.messageId = messageId;
        this.username = username;
        this.reactionType = reactionType;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public Long getMessageId() {
        return messageId;
    }

    public String getUsername() {
        return username;
    }

    public String getReactionType() {
        return reactionType;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setReactionType(String reactionType) {
        this.reactionType = reactionType;
    }
}