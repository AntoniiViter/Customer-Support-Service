package com.example.customersupport.service;

import com.example.customersupport.model.nosql.Conversation;
import com.example.customersupport.model.relational.Client;
import com.example.customersupport.repository.ClientRepository;
import com.example.customersupport.repository.ConversationRepository;
import com.example.customersupport.repository.CorporationRepository;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.customersupport.model.nosql.ConversationStatus.ACTIVE;

@Service
public class ConversationService {

    private final ConversationRepository conversationRepository; // MongoDB repository

    private final ClientRepository clientRepository; // PostgreSQL repository
    private final CorporationRepository corporationRepository;


    public ConversationService(ConversationRepository conversationRepository, ClientRepository clientRepository, CorporationRepository corporationRepository) {
        this.conversationRepository = conversationRepository;
        this.clientRepository = clientRepository;
        this.corporationRepository = corporationRepository;
    }

    // POST: Create a new conversation and associate it with the client
    @Transactional
    public Conversation createConversation(Conversation newConversation) {
        // Save conversation in MongoDB
        Conversation savedConversation = conversationRepository.save(newConversation);

        // Find the client in PostgreSQL
        Client client = clientRepository.findByEmailAndCorporation_CorpName(savedConversation.getClientEmail(), newConversation.getCorpName())
                .orElseThrow(() -> new IllegalArgumentException("Client with email " + savedConversation.getClientEmail() + " not found"));

        // Add the conversation ID to the client in PostgreSQL
        client.getConversationIds().add(savedConversation.getId());
        clientRepository.save(client);

        return savedConversation;
    }

    @Transactional
    public String openConversation(String clientEmail, String corpName) {
        // Create a new Conversation instance and set the provided ID
        Conversation newConversation = new Conversation();
        newConversation.setStatus(ACTIVE.getStatus());  // You can set a default status if needed
        newConversation.setMessages(new ArrayList<>());  // Initialize with an empty list of messages

        // Set client email or anonymous
        String finalClientEmail = (clientEmail != null && !clientEmail.isEmpty()) ? clientEmail : "anonymous@example.com";
        newConversation.setClientEmail(finalClientEmail);
        newConversation.setCreatedAt(LocalDateTime.now());
        newConversation.setCorpName(corpName);

        // Save conversation in MongoDB
        Conversation savedConversation = conversationRepository.save(newConversation);

        // Find or create the client in PostgreSQL
        Client client = clientRepository.findByEmailAndCorporation_CorpName(finalClientEmail, corpName)
                .orElseGet(() -> {
                    // Create a new client if not found
                    Client newClient = new Client();
                    newClient.setEmail(finalClientEmail);
                    newClient.setConversationIds(new ArrayList<>()); // Initialize the conversation ID list
                    newClient.setCorporation(corporationRepository.findByCorpName(corpName).orElseThrow());
                    return clientRepository.save(newClient);  // Save the new client to the database
                });

        // Add the conversation ID to the client in PostgreSQL
        client.getConversationIds().add(savedConversation.getId());
        clientRepository.save(client);

        return savedConversation.getId();
    }

    @Transactional
    public void changeConversationClient(String conversationId, String newClientEmail, String corpName) {
        // Ensure that the conversation exists in MongoDB
        Optional<Conversation> optionalConversation = conversationRepository.findById(conversationId);
        if (optionalConversation.isEmpty()) {
            throw new IllegalArgumentException("Conversation not found with ID: " + conversationId);
        }

        Conversation conversation = optionalConversation.get();

        // Get the current client email (whether anonymous or non-anonymous)
        String currentClientEmail = conversation.getClientEmail();

        // Remove the conversation from the current client (including anonymous or specific user)
        Optional<Client> currentClient = clientRepository.findByEmailAndCorporation_CorpName(currentClientEmail, corpName);
        currentClient.ifPresent(client -> {
            client.getConversationIds().remove(conversationId);

            // If the client is not anonymous and has no more conversations, delete the client
            if (client.getConversationIds().isEmpty() && !client.getEmail().equals("anonymous@example.com")) {
                clientRepository.delete(client);
            } else {
                clientRepository.save(client);  // Save the updated client with removed conversation ID
            }
        });

        // Find or create the new client with the specified email in PostgreSQL
        Client newClient = clientRepository.findByEmailAndCorporation_CorpName(newClientEmail, corpName)
                .orElseGet(() -> {
                    // Create a new client if not found
                    Client client = new Client();
                    client.setEmail(newClientEmail);
                    client.setCorporation(corporationRepository.findByCorpName(corpName)
                            .orElseThrow(() -> new IllegalArgumentException("Corporation not found: " + corpName)));
                    client.setConversationIds(new ArrayList<>());  // Initialize conversation ID list
                    return clientRepository.save(client);
                });

        // Add the conversation ID to the new client in PostgreSQL, ensuring no duplicates
        if (!newClient.getConversationIds().contains(conversationId)) {
            newClient.getConversationIds().add(conversationId);
            clientRepository.save(newClient);  // Save the updated new client
        }

        // Update the clientEmail field in the conversation in MongoDB
        conversation.setClientEmail(newClientEmail);  // Update the client email in MongoDB
        conversationRepository.save(conversation);  // Save the updated conversation
    }


    public void setHeadline(String conversationId, String headline) {
        Optional<Conversation> optionalConversation = conversationRepository.findById(conversationId);

        if (optionalConversation.isPresent()) {
            Conversation conversation = optionalConversation.get();
            conversation.setHeadline(headline);

            conversationRepository.save(conversation);
        } else {
            throw new IllegalArgumentException("Conversation not found with ID: " + conversationId);
        }
    }


    public Conversation getConversationById(String conversationId) {
        return conversationRepository.findById(conversationId)
                .orElseThrow(() -> new IllegalArgumentException("Conversation with ID " + conversationId + " not found"));
    }

    @Transactional
    public Conversation updateConversation(String conversationId, Conversation updatedConversation) {
        Conversation existingConversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new IllegalArgumentException("Conversation with ID " + conversationId + " not found"));

        existingConversation.setStatus(updatedConversation.getStatus());
        existingConversation.setMessages(updatedConversation.getMessages());
        existingConversation.setHeadline(updatedConversation.getHeadline());
        existingConversation.setCreatedAt(updatedConversation.getCreatedAt());

        return conversationRepository.save(existingConversation);
    }

    @Transactional
    public void updateConversationState(String conversationId, String newState) {
        Optional<Conversation> optionalConversation = conversationRepository.findById(conversationId);
        if (optionalConversation.isEmpty()) {
            throw new IllegalArgumentException("Conversation not found with ID: " + conversationId);
        }
        Conversation conversation = optionalConversation.get();

        conversation.setStatus(newState);

        conversationRepository.save(conversation);
    }

    // DELETE: Remove a conversation from MongoDB and PostgreSQL
    @Transactional
    public void deleteConversation(String conversationId) {
        // Delete conversation from MongoDB
        conversationRepository.deleteById(conversationId);

        // Find the client in PostgreSQL
        Client client = clientRepository.findByConversationIdsContaining(conversationId)
                .orElseThrow(() -> new IllegalArgumentException("Client not found"));

        // Remove the conversation ID from the client
        client.getConversationIds().remove(conversationId);
        clientRepository.save(client);

        // If the client's email is not 'anonymous@example.com' and has no other conversations, delete the client
        if (!client.getEmail().equalsIgnoreCase("anonymous@example.com") && client.getConversationIds().isEmpty()) {
            clientRepository.delete(client);
        }
    }

    public List<Conversation> getConversationsByClientEmail(String clientEmail) {
        Client client = clientRepository.findByEmail(clientEmail)
                .orElseThrow(() -> new IllegalArgumentException("Client with email " + clientEmail + " not found"));

        return conversationRepository.findAllById(client.getConversationIds());
    }
}