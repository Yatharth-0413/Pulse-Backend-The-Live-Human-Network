package com.pulse.backend.services;

import com.pulse.backend.dto.CreateMessageRequest;
import com.pulse.backend.dto.TopicStatsResponse;
import com.pulse.backend.dto.TypingEvent;
import com.pulse.backend.enums.RewardType;
import com.pulse.backend.models.Message;
import com.pulse.backend.models.Reaction;
import com.pulse.backend.repositories.MessageRepository;
import com.pulse.backend.repositories.ReactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import com.pulse.backend.entity.User;
import com.pulse.backend.repositories.UserRepository;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class MessageService {

    private final Map<String, List<Message>> topicMessages = new HashMap<>();
    private final Map<String, Set<String>> activeUsersByTopic = new HashMap<>();

    private final MessageRepository messageRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final UserRepository userRepository;
    private final ReactionRepository reactionRepository;
    private final UserService userService;

    public MessageService(MessageRepository messageRepository,SimpMessagingTemplate messagingTemplate,UserRepository userRepository,ReactionRepository reactionRepository,UserService userService) {
        this.messageRepository = messageRepository;
        this.messagingTemplate = messagingTemplate;
        this.userRepository = userRepository;
        this.reactionRepository = reactionRepository;
        this.userService = userService;
        if (messageRepository.count() == 0) {
            messageRepository.save(new Message("Startup Fear", "SilentTiger42", "I am scared about my future."));
            messageRepository.save(new Message("Startup Fear", "BraveSoul17", "You are not alone."));
            messageRepository.save(new Message("Career Stress", "LostEngineer91", "Corporate life is exhausting."));
        }
    }

    public List<Message> getMessages(String topicName) {
        return messageRepository.findByTopicNameOrderByIdAsc(topicName);
    }

    public Message createMessage(String topicName, CreateMessageRequest request) {
        userRepository.findByUsername(request.getUsername()).orElseGet(() -> userRepository.save(new User(request.getUsername())));

        Message message = new Message(topicName, request.getUsername(), request.getText());
        Message savedMessage = messageRepository.save(message);
//        userService.addReputation(request.getUsername(), 1);
        userService.reward(request.getUsername(), 1, RewardType.MESSAGE_CREATED, "Posted a message");

        messagingTemplate.convertAndSend("/topic/" + topicName, savedMessage);
        System.out.println("PUBLISHED TO: /topic/" + topicName);

        return savedMessage;
    }

    public TopicStatsResponse getTopicStats(String topicName) {

        List<Message> messages = messageRepository.findByTopicNameOrderByIdAsc(topicName);

        int messageCount = messages.size();

        Set<String> uniqueUsers = new HashSet<>();

        for (Message message : messages) {
            uniqueUsers.add(message.getUsername());
        }

        int activeUsers = activeUsersByTopic.getOrDefault(topicName, new HashSet<>()).size();

        return new TopicStatsResponse(activeUsers, messageCount);
    }

    public void joinTopic(String topicName, String username) {
        activeUsersByTopic.computeIfAbsent(topicName, k -> new HashSet<>()).add(username);
        messagingTemplate.convertAndSend("/topic/" + topicName, "USER_JOINED");
    }

    public void leaveTopic(String topicName, String username) {
        Set<String> users = activeUsersByTopic.get(topicName);
        if (users != null) {users.remove(username);}
        messagingTemplate.convertAndSend("/topic/" + topicName, "USER_LEFT");
    }


//    public void reactToMessage(Long messageId,String username, String reactionType){
//        Message message = messageRepository.findById(messageId).orElseThrow();
//
//        switch (reactionType) {
//
//            case "HEART":
//                message.setHeartCount(
//                        message.getHeartCount() + 1
//                );
//                break;
//
//            case "FIRE":
//                message.setFireCount(
//                        message.getFireCount() + 1
//                );
//                break;
//
//            case "SAD":
//                message.setSadCount(
//                        message.getSadCount() + 1
//                );
//                break;
//
//            case "SUPPORT":
//                message.setSupportCount(
//                        message.getSupportCount() + 1
//                );
//                break;
//        }
//        Message updatedMessage = messageRepository.save(message);
//
//        messagingTemplate.convertAndSend("/topic/" + updatedMessage.getTopicName(), updatedMessage);
//    }

    public void reactToMessage(Long messageId, String username, String reactionType) {

        Message message = messageRepository.findById(messageId).orElseThrow();

        /*
         * Rule 1
         * No self reaction
         */
        if (message.getUsername().equalsIgnoreCase(username)) {
            return;
        }

        Reaction existingReaction = reactionRepository.findByMessageIdAndUsername(messageId, username).orElse(null);
        /*
         * Rule 2
         * Same reaction again
         */
        if (existingReaction != null && existingReaction.getReactionType().equals(reactionType)) {
            return;
        }

        /*
         * Rule 3
         * User already reacted
         * remove previous count
         */
        if (existingReaction != null) {
            decrementReactionCount(message, existingReaction.getReactionType());
            existingReaction.setReactionType(reactionType);
            reactionRepository.save(existingReaction);
        }
        else {
            Reaction reaction = new Reaction(messageId, username, reactionType);
            reactionRepository.save(reaction);
            String messageOwner = message.getUsername();
            switch (reactionType) {
                case "HEART":
//                    userService.addReputation(messageOwner, 2);
                    userService.reward(messageOwner, 2, RewardType.HEART_RECEIVED, "Received ❤️ on a message");
                    break;

                case "FIRE":
//                    userService.addReputation(messageOwner, 3);
                    userService.reward(messageOwner, 3, RewardType.FIRE_RECEIVED, "Received 🔥 on a message");
                    break;

                case "SAD":
//                    userService.addReputation(messageOwner, 1);
                    userService.reward(messageOwner, 1, RewardType.SAD_RECEIVED, "Received 😢 on a message");
                    break;

                case "SUPPORT":
//                    userService.addReputation(messageOwner, 4);
                    userService.reward(messageOwner, 4, RewardType.SUPPORT_RECEIVED, "Received 🤝 on a message");
                    break;
            }
        }

        incrementReactionCount(message, reactionType);

        Message updatedMessage = messageRepository.save(message);


        messagingTemplate.convertAndSend("/topic/" + updatedMessage.getTopicName(), updatedMessage);
    }

    private void incrementReactionCount(Message message, String type) {
        switch (type) {
            case "HEART":
                message.setHeartCount(message.getHeartCount() + 1);
                break;

            case "FIRE":
                message.setFireCount(message.getFireCount() + 1);
                break;

            case "SAD":
                message.setSadCount(message.getSadCount() + 1);
                break;

            case "SUPPORT":
                message.setSupportCount(message.getSupportCount() + 1);
                break;
        }
    }


    private void decrementReactionCount(Message message, String type) {
        switch (type) {
            case "HEART":
                message.setHeartCount(Math.max(0, message.getHeartCount() - 1));
                break;

            case "FIRE":
                message.setFireCount(Math.max(0, message.getFireCount() - 1));
                break;

            case "SAD":
                message.setSadCount(Math.max(0, message.getSadCount() - 1));
                break;

            case "SUPPORT":
                message.setSupportCount(Math.max(0, message.getSupportCount() - 1));
                break;
        }
    }

    public int getHeatScore(String topicName) {

        LocalDateTime oneHourAgo = LocalDateTime.now().minusHours(1);

        List<Message> recentMessages = messageRepository.findByTopicNameAndCreatedAtAfter(topicName, oneHourAgo);
        int messageCount = recentMessages.size();
        int activeUsers = activeUsersByTopic.getOrDefault(topicName, new HashSet<>()).size();

        int totalReactions = 0;

        for (Message message : recentMessages) {
            totalReactions += message.getHeartCount();
            totalReactions += message.getFireCount();
            totalReactions += message.getSadCount();
            totalReactions += message.getSupportCount();
        }

        double score = (activeUsers * 5) + (messageCount * 3) + (totalReactions * 2);
        return (int) score;
    }

    public void sendTypingEvent(String topicName, String username) {
        messagingTemplate.convertAndSend("/topic/" + topicName + "/typing", username);
    }
}