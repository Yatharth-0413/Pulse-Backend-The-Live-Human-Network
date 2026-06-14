package com.pulse.backend.services;

import com.pulse.backend.dto.ActivityResponse;
import com.pulse.backend.dto.UserProfileResponse;
import com.pulse.backend.entity.User;
import com.pulse.backend.models.Message;
import com.pulse.backend.repositories.MessageRepository;
import com.pulse.backend.repositories.TopicRepository;
import com.pulse.backend.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfileService {

    private final UserRepository userRepository;
    private final MessageRepository messageRepository;
    private final TopicRepository topicRepository;
    private final ActivityService activityService;

    public ProfileService(UserRepository userRepository, MessageRepository messageRepository, TopicRepository topicRepository, ActivityService activityService) {
        this.userRepository = userRepository;
        this.messageRepository = messageRepository;
        this.topicRepository = topicRepository;
        this.activityService = activityService;
    }

    public UserProfileResponse getProfile(String username) {

        User user = userRepository
                        .findByUsername(username)
                        .orElseGet(() -> userRepository.save(new User(username)));

        long messageCount = messageRepository.countByUsername(username);
        int topicCount = (int) topicRepository.countByCreatedBy(username);

        List<Message> messages = messageRepository.findByUsername(username);

        long reactionCount = 0;

        for (Message message : messages) {
            reactionCount += message.getHeartCount();
            reactionCount += message.getFireCount();
            reactionCount += message.getSadCount();
            reactionCount += message.getSupportCount();
        }

        List<ActivityResponse> activities = activityService.getActivities(username).stream()
                .map(activity ->
                        new ActivityResponse(activity.getDescription(), activity.getPoints(), activity.getCreatedAt()))
                .toList();

        return new UserProfileResponse(
                user.getUsername(),
                user.getJoinedAt(),
                messageCount,
                topicCount,
                reactionCount,
                user.getReputation(),
                user.getBadge(),
                activities
        );
    }
}