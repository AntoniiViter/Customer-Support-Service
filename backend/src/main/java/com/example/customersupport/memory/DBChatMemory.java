package com.example.customersupport.memory;

import com.example.customersupport.model.nosql.Conversation;
import com.example.customersupport.model.nosql.ConversationMessage;
import com.example.customersupport.model.nosql.ConversationMessageType;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import com.example.customersupport.repository.ConversationRepository;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
public class DBChatMemory implements ChatMemory {

    private final ConversationRepository conversationRepository;


    public DBChatMemory(ConversationRepository conversationRepository) {
        this.conversationRepository = conversationRepository;
    }

    @Override
    public void add(String conversationId, List<Message> messages) {
        // Retrieve the conversation by ID
        Optional<Conversation> optionalConversation = conversationRepository.findById(conversationId);

        if (optionalConversation.isPresent()) {
            // If conversation exists, append new messages
            Conversation conversation = optionalConversation.get();
            conversation.getMessages().addAll(messages.stream().map(m -> new ConversationMessage(m.getContent(), m.getMessageType().getValue())).toList());
            conversationRepository.save(conversation);  // Save updated conversation
        } else {
            // If conversation doesn't exist, create a new one
            Conversation newConversation = new Conversation();
            newConversation.setId(conversationId);
            newConversation.setMessages(messages.stream().map(m -> new ConversationMessage(m.getContent(), m.getMessageType().getValue())).toList());
            newConversation.setCreatedAt(LocalDateTime.now());
            newConversation.setStatus("active");
            conversationRepository.save(newConversation);  // Save new conversation
        }
    }

    @Override
    public List<Message> get(String conversationId, int lastN) {
        // Retrieve the conversation by ID
        Optional<Conversation> optionalConversation = conversationRepository.findById(conversationId);

        if (optionalConversation.isPresent()) {
            // Get the list of messages
            List<Message> messages = optionalConversation.get().getMessages().stream().map(s -> {
                if(Objects.equals(s.getSender(), ConversationMessageType.ASSISTANT.getType())) {
                    return (Message) new AssistantMessage(s.getContent());
                }
                else {
                    return (Message) new UserMessage(s.getContent());
                }
            }).toList();
            // Return the last N messages
            return messages.size() <= lastN ? messages : messages.subList(messages.size() - lastN, messages.size());
        }
        return new ArrayList<>();  // Return empty list if conversation not found
    }

    @Override
    public void clear(String conversationId) {
        // Retrieve the conversation by ID
        Optional<Conversation> optionalConversation = conversationRepository.findById(conversationId);

        if (optionalConversation.isPresent()) {
            // Clear all messages in the conversation
            Conversation conversation = optionalConversation.get();
            conversation.setMessages(new ArrayList<>());  // Clear messages
            conversation.setStatus("closed");
            conversationRepository.save(conversation);  // Save updated conversation
        }
    }

    public void delete(String conversationId) {
        // Delete the conversation from the database by ID
        conversationRepository.deleteById(conversationId);
    }
}