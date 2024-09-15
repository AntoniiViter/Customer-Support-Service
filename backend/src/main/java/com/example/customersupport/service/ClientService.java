package com.example.customersupport.service;

import com.example.customersupport.model.relational.Client;
import com.example.customersupport.repository.ClientRepository;
import com.example.customersupport.repository.ConversationRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClientService {

    private final ClientRepository clientRepository;


    private final ConversationRepository conversationRepository;

    public ClientService(ConversationRepository conversationRepository, ClientRepository clientRepository) {
        this.conversationRepository = conversationRepository;
        this.clientRepository = clientRepository;
    }

    public Client createClient(Client client) {
        return clientRepository.save(client);
    }

    public Optional<Client> getClientById(Long clientId) {
        return clientRepository.findById(clientId);
    }

    public Client updateClient(Long clientId, Client updatedClient) {
        Optional<Client> existingClient = clientRepository.findById(clientId);
        if (existingClient.isPresent()) {
            Client client = existingClient.get();
            client.setEmail(updatedClient.getEmail());
            client.setName(updatedClient.getName());
            return clientRepository.save(client);
        } else {
            throw new IllegalArgumentException("Client not found with ID: " + clientId);
        }
    }

    // Delete a Client by ID and remove all associated conversations in MongoDB
    @Transactional
    public void deleteClient(Long clientId) {
        // Find the client in PostgreSQL
        Optional<Client> optionalClient = clientRepository.findById(clientId);

        if (optionalClient.isPresent()) {
            Client client = optionalClient.get();

            // Get all conversation IDs associated with this client
            List<String> conversationIds = client.getConversationIds();

            // Delete all associated conversations in MongoDB
            for (String conversationId : conversationIds) {
                conversationRepository.deleteById(conversationId);
            }

            // Delete the client from PostgreSQL
            clientRepository.deleteById(clientId);

            System.out.println("Client and associated conversations deleted successfully.");
        } else {
            throw new IllegalArgumentException("Client with ID " + clientId + " not found.");
        }
    }

    public Optional<Client> findClientByConversationId(String conversationId) {
        return clientRepository.findByConversationIdsContaining(conversationId);
    }
}