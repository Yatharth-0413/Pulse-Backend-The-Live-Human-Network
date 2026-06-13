package com.pulse.backend.controllers;

import com.pulse.backend.dto.UserProfileResponse;
import com.pulse.backend.services.ProfileService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(
            ProfileService profileService) {

        this.profileService = profileService;
    }

    @GetMapping("/{username}")
    public UserProfileResponse getProfile(
            @PathVariable
            String username) {

        return profileService.getProfile(
                username
        );
    }
}