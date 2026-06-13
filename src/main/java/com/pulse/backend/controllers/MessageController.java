package com.pulse.backend.controllers;


import com.pulse.backend.dto.*;
import com.pulse.backend.models.Message;
import com.pulse.backend.services.MessageService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/topics")
public class MessageController {

    private final MessageService messageService;

    public MessageController(
            MessageService messageService) {

        this.messageService = messageService;
    }

    @GetMapping("/{topicName}/messages")
    public List<Message> getMessages(
            @PathVariable String topicName) {

        return messageService.getMessages(topicName);
    }

    @PostMapping("/{topicName}/messages")
    public Message createMessage(
            @PathVariable String topicName,
            @RequestBody CreateMessageRequest request) {
        System.out.println(
                "Requested Topic = " + topicName);

        return messageService.createMessage(
                topicName,
                request
        );
    }

    @GetMapping("/{topicName}/stats")
    public TopicStatsResponse getTopicStats(
            @PathVariable String topicName) {

        return messageService.getTopicStats(
                topicName
        );
    }

    @PostMapping("/{topicName}/join")
    public void joinTopic(
            @PathVariable String topicName,
            @RequestBody JoinTopicRequest request) {

        messageService.joinTopic(
                topicName,
                request.getUsername()
        );
    }

    @PostMapping("/{topicName}/leave")
    public void leaveTopic(
            @PathVariable String topicName,
            @RequestBody JoinTopicRequest request) {

        messageService.leaveTopic(
                topicName,
                request.getUsername()
        );
    }


    @PostMapping("/messages/{messageId}/react")
    public void reactToMessage(@PathVariable Long messageId, @RequestBody ReactionRequest request) {
        messageService.reactToMessage(messageId, request.getUsername(), request.getType());
    }

    @PostMapping("/{topicName}/typing")
    public void typing(@PathVariable String topicName, @RequestBody TypingEvent request) {
        messageService.sendTypingEvent(topicName, request.getUsername());
    }
}