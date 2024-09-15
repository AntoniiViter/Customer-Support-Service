package com.example.customersupport.mock;

import com.example.customersupport.model.nosql.Conversation;
import com.example.customersupport.model.relational.Client;
import com.example.customersupport.repository.ClientRepository;
import com.example.customersupport.repository.ConversationRepository;
import com.example.customersupport.service.ConversationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ConversationServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private ConversationRepository conversationRepository;

    @InjectMocks
    private ConversationService conversationService;  //Service under test

    @Test
    void testOpenConversation() {
        // Arrange
        String clientEmail = "client@example.com";
        String corpName = "TestCorp";
        Conversation mockConversation = new Conversation();
        mockConversation.setId("12345");
        mockConversation.setCorpName(corpName);

        Client mockClient = new Client();
        mockClient.setEmail(clientEmail);
        mockClient.setConversationIds(new ArrayList<>());

        when(conversationRepository.save(any(Conversation.class))).thenReturn(mockConversation);
        when(clientRepository.findByEmailAndCorporation_CorpName(clientEmail, corpName)).thenReturn(Optional.of(mockClient));
        when(clientRepository.save(any(Client.class))).thenReturn(mockClient);

        // Act
        String result = conversationService.openConversation(clientEmail, corpName);

        // Assert
        assertEquals("12345", result);
        verify(conversationRepository).save(any(Conversation.class));
        verify(clientRepository).findByEmailAndCorporation_CorpName(clientEmail, corpName);
        verify(clientRepository).save(any(Client.class));
    }
}