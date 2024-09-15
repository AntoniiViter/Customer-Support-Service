package com.example.customersupport.repository;

import com.example.customersupport.model.nosql.Conversation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ConversationRepository extends MongoRepository<Conversation, String> {
    Optional<Conversation> findByClientEmailAndStatusIn(String client, List<String> statuses);
    List<Conversation> findByStatus(String status);
    List<Conversation> findByStatusAndCreatedAtBefore(String status, LocalDateTime date);
}