package com.pulse.backend.dto;

public class TopicStatsResponse {

    private long activeUsers;
    private long messageCount;

    public TopicStatsResponse(long activeUsers, long messageCount) {
        this.activeUsers = activeUsers;
        this.messageCount = messageCount;
    }

    public long getActiveUsers() {
        return activeUsers;
    }

    public long getMessageCount() {
        return messageCount;
    }
}