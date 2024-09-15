package com.example.customersupport.service;

import com.example.customersupport.model.nosql.Conversation;
import com.example.customersupport.repository.ClientRepository;
import com.example.customersupport.repository.ConversationRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import com.example.customersupport.model.relational.Client;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.example.customersupport.model.nosql.ConversationStatus.*;

@Service
public class CleanUpService {

    @Value("${escalate_or_close.conversation.interval.minutes}")
    private long intervalEscalateOrClose;

    @Value("${delete_closed.conversation.interval.days}")
    private long intervalDeleteClosed;


    private final ConversationRepository conversationRepository;
    private final ClientRepository clientRepository;

    public CleanUpService(ConversationRepository conversationRepository, ClientRepository clientRepository) {
        this.conversationRepository = conversationRepository;
        this.clientRepository = clientRepository;
    }

    // Method to update or escalate conversations that have been active for more than a given interval
    @Scheduled(fixedRate = 1800000)  // Example: Run every 30 minutes
    public void escalateOrCloseConversations() {
        Duration interval = Duration.ofMinutes(intervalEscalateOrClose);
        LocalDateTime threshold = LocalDateTime.now().minus(interval);

        // Get active conversations that are older than the given interval
        List<Conversation> activeConversations = conversationRepository.findByStatusAndCreatedAtBefore(ACTIVE.getStatus(), threshold);

        for (Conversation conversation : activeConversations) {
            if (conversation.getMessages().isEmpty()) {
                // If conversation has no messages, delete it
                conversationRepository.delete(conversation);

                // Also remove from the client in PostgreSQL
                removeConversationFromClient(conversation.getId());
            }
            else if (ACTIVE.getStatus().equals(conversation.getStatus())) {
                // Escalate the conversation
                if (conversation.getClientEmail() == null || conversation.getClientEmail().isEmpty() || conversation.getClientEmail().equals("anonymous@example.com")) {
                    conversation.setStatus(FAILED.getStatus());
                } else {
                    conversation.setStatus(ESCALATED.getStatus());
                }
                
                conversation.setStatus(ESCALATED.getStatus());
                conversationRepository.save(conversation);
            }
        }

        System.out.println("Updated or deleted conversations older than " + interval.toHours() + " hours at " + LocalDateTime.now());
    }

    // Method to delete conversations that have been closed for more than 30 days
    @Scheduled(fixedRate = 86400000)  // Run daily
    public void deleteClosedConversationsAfter30Days() {
        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(intervalDeleteClosed);

        // Find closed conversations that were created more than 30 days ago
        List<Conversation> closedConversations = conversationRepository.findByStatusAndCreatedAtBefore(CLOSED.getStatus(), thirtyDaysAgo);

        for (Conversation conversation : closedConversations) {
            conversationRepository.delete(conversation);
            removeConversationFromClient(conversation.getId());
        }

        System.out.println("Deleted closed conversations older than 30 days at " + LocalDateTime.now());
    }

    // Helper method to remove conversation from the client in PostgreSQL
    private void removeConversationFromClient(String conversationId) {
        // Find the client by conversation ID (since conversationIds are unique)
        Optional<Client> optionalClient = clientRepository.findByConversationIdsContaining(conversationId);

        if (optionalClient.isPresent()) {
            Client client = optionalClient.get();
            client.getConversationIds().remove(conversationId); // Remove the conversation ID from the client's list
            clientRepository.save(client); // Save the updated client entity
        }
    }
}