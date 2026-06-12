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

    public TopicService( MessageService messageService, TopicRepository topicRepository) {
        this.messageService = messageService;
        this.topicRepository = topicRepository;

        if (topicRepository.count() == 0) {
            topicRepository.save(new Topic("Startup Fear", 248, 0));
            topicRepository.save(new Topic("Career Stress", 512, 0));
            topicRepository.save(new Topic("Relationships", 890, 0));
            topicRepository.save(new Topic("Loneliness", 356, 0));
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

    public Topic createTopic(String topicName) {
        if (topicExists(topicName)) {
            return topicRepository
                    .findAll()
                    .stream()
                    .filter(topic -> topic.getName().equalsIgnoreCase(topicName))
                    .findFirst()
                    .orElseThrow();
        }
        Topic topic = new Topic(topicName, 0, 0);
        return topicRepository.save(topic);
    }

    public boolean topicExists(String topicName) {
        return topicRepository.findAll().stream().anyMatch(topic -> topic.getName().equalsIgnoreCase(topicName));
    }
}