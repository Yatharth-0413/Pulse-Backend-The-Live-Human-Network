package com.pulse.backend.dto;

import java.time.LocalDateTime;

public class UserProfileResponse {

    private String username;
    private LocalDateTime memberSince;

    private long messageCount;
    private int topicCount;
    private long reactionCount;

    private int reputation;
    private String badge;

    public UserProfileResponse(
            String username,
            LocalDateTime memberSince,
            long messageCount,
            int topicCount,
            long reactionCount,
            int reputation,
            String badge) {

        this.username = username;
        this.memberSince = memberSince;
        this.messageCount = messageCount;
        this.topicCount = topicCount;
        this.reactionCount = reactionCount;
        this.reputation = reputation;
        this.badge = badge;
    }

    public String getUsername() {
        return username;
    }

    public LocalDateTime getMemberSince() {
        return memberSince;
    }

    public long getMessageCount() {
        return messageCount;
    }

    public long getReactionCount() {
        return reactionCount;
    }

    public int getReputation() {
        return reputation;
    }

    public String getBadge() {
        return badge;
    }

    public int getTopicCount() {
        return topicCount;
    }
}