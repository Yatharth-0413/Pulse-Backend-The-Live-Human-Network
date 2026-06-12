package com.pulse.backend.models;

import lombok.Setter;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String topicName;
    private String username;
    private String text;
    private LocalDateTime createdAt;
    @Setter
    private int heartCount;
    @Setter
    private int fireCount;
    @Setter
    private int sadCount;
    @Setter
    private int supportCount;

    public Message() {
    }
    public Message(String topicName, String username, String text) {
        this.topicName = topicName;
        this.username = username;
        this.text = text;
        this.createdAt = LocalDateTime.now();
        this.heartCount=0;
        this.fireCount=0;
        this.sadCount=0;
        this.supportCount=0;
    }


    public String getUsername() {
        return username;
    }

    public String getText() {
        return text;
    }

    public int getHeartCount() {
        return heartCount;
    }

    public int getFireCount() {
        return fireCount;
    }

    public int getSadCount() {
        return sadCount;
    }

    public int getSupportCount() {
        return supportCount;
    }
    public Long getId() {
        return id;
    }

    public String getTopicName() {
        return topicName;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}