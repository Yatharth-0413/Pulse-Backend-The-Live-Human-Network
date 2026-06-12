package com.pulse.backend.dto;

public class TypingEvent {

    private String username;

    public TypingEvent() {
    }

    public TypingEvent(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}