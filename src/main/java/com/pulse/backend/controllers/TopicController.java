package com.pulse.backend.controllers;

import com.pulse.backend.dto.CreateTopicRequest;
import com.pulse.backend.models.Topic;
import com.pulse.backend.services.TopicService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/topics")
public class TopicController {

    private final TopicService topicService;

    public TopicController(
            TopicService topicService) {

        this.topicService = topicService;
    }

    @GetMapping
    public List<Topic> getTopics() {
        return topicService.getTopics();
    }

    @PostMapping
    public Topic createTopic(
            @RequestBody
            CreateTopicRequest request) {

        return topicService.createTopic(
                request.getName()
        );
    }
}