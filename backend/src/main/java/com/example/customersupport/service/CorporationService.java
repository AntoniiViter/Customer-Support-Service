package com.example.customersupport.service;

import com.example.customersupport.dto.CorporationRegistrationDto;
import com.example.customersupport.model.auth.AuthGrantedAuthority;
import com.example.customersupport.model.relational.Client;
import com.example.customersupport.model.relational.Corporation;
import com.example.customersupport.repository.AuthGrantedAuthorityRepository;
import com.example.customersupport.repository.ClientRepository;
import com.example.customersupport.repository.ConversationRepository;
import com.example.customersupport.repository.CorporationRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class CorporationService {


    private final CorporationRepository corporationRepository;  // PostgreSQL repository for corporations
    private final ClientRepository clientRepository;  // PostgreSQL repository for clients
    private final ConversationRepository conversationRepository;  // MongoDB repository for conversations
    private final PasswordEncoder passwordEncoder;
    private final AuthGrantedAuthorityRepository authGrantedAuthorityRepository;

    public CorporationService(CorporationRepository corporationRepository, ClientRepository clientRepository, ConversationRepository conversationRepository, PasswordEncoder passwordEncoder, AuthGrantedAuthorityRepository authGrantedAuthorityRepository) {
        this.corporationRepository = corporationRepository;
        this.clientRepository = clientRepository;
        this.conversationRepository = conversationRepository;
        this.passwordEncoder = passwordEncoder;
        this.authGrantedAuthorityRepository = authGrantedAuthorityRepository;
    }

    public Corporation registerNewCorporation(CorporationRegistrationDto registrationDto) {
        // Check if a corporation with the same name or email already exists
        if (corporationRepository.findByCorpName(registrationDto.getCorpName()).isPresent()) {
            throw new IllegalArgumentException("Corporation name is already taken");
        }
        if (corporationRepository.findByEmail(registrationDto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email is already registered");
        }

        // Create a new Corporation instance and save it
        Corporation corporation = new Corporation();
        corporation.setCorpName(registrationDto.getCorpName());
        corporation.setEmail(registrationDto.getEmail());
        corporation.setPassword(passwordEncoder.encode(registrationDto.getPassword())); // Encrypt password

        return corporationRepository.save(corporation);
    }

    public Optional<Corporation> getCorporationByCorpName(String corpName) {
        return corporationRepository.findByCorpName(corpName);
    }

    public Corporation updateCorporation(String corpName, Corporation updatedCorporation) {
        Optional<Corporation> existingCorporation = corporationRepository.findByCorpName(corpName);
        if (existingCorporation.isPresent()) {
            Corporation corporation = existingCorporation.get();
            corporation.setCorpName(updatedCorporation.getCorpName());
            corporation.setEmail(updatedCorporation.getEmail());
            return corporationRepository.save(corporation);
        } else {
            throw new IllegalArgumentException("Corporation not found with corpName: " + corpName);
        }
    }

    // Delete a Corporation by corpName and all conversations of its clients in MongoDB
    @Transactional
    public void deleteCorporation(String corpName) {
        Optional<Corporation> existingCorporation = corporationRepository.findByCorpName(corpName);

        if (existingCorporation.isPresent()) {
            Corporation corporation = existingCorporation.get();

            // Set all authorities' corporation reference to null
            Set<AuthGrantedAuthority> authorities = corporation.getAuthorities();
            if (authorities != null) {
                for (AuthGrantedAuthority authority : authorities) {
                    authority.setCorporation(null);
                }
                // Save the updated authorities
                authGrantedAuthorityRepository.saveAll(authorities);
            }



            // Fetch all clients associated with the corporation
            List<Client> clients = clientRepository.findByCorporation(corporation);

            // Delete all conversations of each client from MongoDB
            for (Client client : clients) {
                List<String> conversationIds = client.getConversationIds();

                for (String conversationId : conversationIds) {
                    conversationRepository.deleteById(conversationId);  // Delete conversation from MongoDB
                }

                clientRepository.delete(client);
            }

            // Delete the corporation from PostgreSQL
            corporationRepository.delete(corporation);

            System.out.println("Corporation and all associated conversations of clients deleted successfully.");
        } else {
            throw new IllegalArgumentException("Corporation not found with corpName: " + corpName);
        }
    }
}