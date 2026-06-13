package com.pulse.backend.services;

import com.pulse.backend.models.Topic;
import com.pulse.backend.repositories.TopicRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TopicService {

    //private final List<Topic> topics = new ArrayList<>();
    private final MessageService messageService;
    private final TopicRepository topicRepository;
    private final UserService userService;

    public TopicService( MessageService messageService, TopicRepository topicRepository, UserService userService) {
        this.messageService = messageService;
        this.topicRepository = topicRepository;
        this.userService = userService;

        if (topicRepository.count() == 0) {
            topicRepository.save(new Topic("Startup Fear","PulseSystem", 248, 0));
            topicRepository.save(new Topic("Career Stress","PulseSystem", 512, 0));
            topicRepository.save(new Topic("Relationships","PulseSystem", 890, 0));
            topicRepository.save(new Topic("Loneliness","PulseSystem", 356, 0));
        }
    }

    public List<Topic> getTopics() {
        List<Topic> topics = topicRepository.findAll();
        for (Topic topic : topics) {
            int heatScore = messageService.getHeatScore(topic.getName());
            topic.setHeatScore(heatScore);
        }

        return topics;
    }

    public Topic createTopic(String topicName,String username) {
        if (topicExists(topicName)) {
            return topicRepository
                    .findAll()
                    .stream()
                    .filter(topic -> topic.getName().equalsIgnoreCase(topicName))
                    .findFirst()
                    .orElseThrow();
        }
        Topic topic = new Topic(topicName,username, 0, 0);
        Topic savedTopic = topicRepository.save(topic);
        userService.addReputation(username, 5);
        return savedTopic;
    }

    public boolean topicExists(String topicName) {
        return topicRepository.findAll().stream().anyMatch(topic -> topic.getName().equalsIgnoreCase(topicName));
    }
}