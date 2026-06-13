package com.pulse.backend.repositories;

import com.pulse.backend.models.Topic;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TopicRepository extends JpaRepository<Topic, Long> {
    long countByCreatedBy(String createdBy);
}