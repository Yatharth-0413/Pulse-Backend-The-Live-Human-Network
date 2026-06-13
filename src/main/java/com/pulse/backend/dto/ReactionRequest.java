package com.pulse.backend.dto;

public class ReactionRequest {

    private String username;
    private String type;

    public String getUsername() {
        return username;
    }

    public void setUsername(
            String username
    ) {
        this.username = username;
    }

    public String getType() {
        return type;
    }

    public void setType(
            String type
    ) {
        this.type = type;
    }
}