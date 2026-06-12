package com.pulse.backend.services;

import com.pulse.backend.dto.CreateMessageRequest;
import com.pulse.backend.dto.TopicStatsResponse;
import com.pulse.backend.dto.TypingEvent;
import com.pulse.backend.models.Message;
import com.pulse.backend.repositories.MessageRepository;
import org.springframework.stereotype.Service;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class MessageService {

    private final Map<String, List<Message>> topicMessages = new HashMap<>();
    private final Map<String, Set<String>> activeUsersByTopic = new HashMap<>();

    private final MessageRepository messageRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public MessageService(MessageRepository messageRepository,SimpMessagingTemplate messagingTemplate) {
        this.messageRepository = messageRepository;
        this.messagingTemplate = messagingTemplate;
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
        Message message = new Message(topicName, request.getUsername(), request.getText());
        Message savedMessage = messageRepository.save(message);
        messagingTemplate.convertAndSend("/topic/" + topicName, savedMessage);
        System.out.println(
                "PUBLISHED TO: /topic/"
                        + topicName
        );
        return savedMessage;
    }

    public TopicStatsResponse getTopicStats(String topicName) {

        List<Message> messages = messageRepository.findByTopicNameOrderByIdAsc(topicName);

        int messageCount = messages.size();

        Set<String> uniqueUsers =
                new HashSet<>();

        for (Message message : messages) {
            uniqueUsers.add(
                    message.getUsername()
            );
        }

        int activeUsers =
                activeUsersByTopic
                        .getOrDefault(
                                topicName,
                                new HashSet<>()
                        )
                        .size();
//        System.out.println(
//                activeUsersByTopic
//        );
        return new TopicStatsResponse(
                activeUsers,
                messageCount
        );
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


    public void reactToMessage(Long messageId, String reactionType){
        Message message = messageRepository.findById(messageId).orElseThrow();

        switch (reactionType) {

            case "HEART":
                message.setHeartCount(
                        message.getHeartCount() + 1
                );
                break;

            case "FIRE":
                message.setFireCount(
                        message.getFireCount() + 1
                );
                break;

            case "SAD":
                message.setSadCount(
                        message.getSadCount() + 1
                );
                break;

            case "SUPPORT":
                message.setSupportCount(
                        message.getSupportCount() + 1
                );
                break;
        }
        Message updatedMessage = messageRepository.save(message);

        messagingTemplate.convertAndSend("/topic/" + updatedMessage.getTopicName(), updatedMessage);
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