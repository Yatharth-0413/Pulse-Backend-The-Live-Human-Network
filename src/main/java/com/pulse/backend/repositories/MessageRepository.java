package com.pulse.backend.repositories;

import com.pulse.backend.models.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findByTopicNameOrderByIdAsc(String topicName);

    List<Message> findByTopicNameAndCreatedAtAfter(String topicName, LocalDateTime time);

    long countByUsername(String username);

    List<Message> findByUsername(String username);

    Optional<Message> findById(Long id);
}