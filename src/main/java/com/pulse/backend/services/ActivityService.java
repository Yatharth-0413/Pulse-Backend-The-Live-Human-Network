package com.pulse.backend.services;

import com.pulse.backend.entity.Activity;
import com.pulse.backend.enums.RewardType;
import com.pulse.backend.repositories.ActivityRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActivityService {

    private final ActivityRepository repository;

    public ActivityService(ActivityRepository repository) {
        this.repository = repository;
    }

    public void logActivity(String username, RewardType rewardType, String description, int points) {
        Activity activity = new Activity(username, rewardType.name(), description, points);
        repository.save(activity);
    }

    public List<Activity> getActivities(String username) {
        return repository.findTop20ByUsernameOrderByCreatedAtDesc(username);
    }
}