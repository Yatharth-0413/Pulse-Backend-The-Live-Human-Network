package com.pulse.backend.repositories;

import com.pulse.backend.entity.Activity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActivityRepository extends JpaRepository<Activity, Long> {
    List<Activity> findTop20ByUsernameOrderByCreatedAtDesc(String username);
}