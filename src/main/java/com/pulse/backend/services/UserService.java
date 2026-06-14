package com.pulse.backend.services;

import com.pulse.backend.entity.User;
import com.pulse.backend.enums.RewardType;
import com.pulse.backend.repositories.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final ActivityService activityService;

    public UserService(UserRepository userRepository, ActivityService activityService) {
        this.activityService = activityService;
        this.userRepository = userRepository;
    }

    public void addReputation(String username, int points) {
        User user = userRepository.findByUsername(username).orElseThrow();
        user.setReputation(user.getReputation() + points);
        user.setBadge(calculateBadge(user.getReputation()));
        userRepository.save(user);
    }

    public void reward(String username, int points, RewardType rewardType, String description) {
        addReputation(username, points);
        activityService.logActivity(username, rewardType, description, points);
        // Activity logging comes next sprint step.
    }

    private String calculateBadge(int reputation) {

        if (reputation >= 1000) {
            return "Community Pillar";
        }

        if (reputation >= 500) {
            return "Pulse Guide";
        }

        if (reputation >= 200) {
            return "Supporter";
        }

        if (reputation >= 50) {
            return "Contributor";
        }

        return "Newcomer";
    }
}