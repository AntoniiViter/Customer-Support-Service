package com.example.customersupport.controller;

import com.example.customersupport.service.CorporationService;
import jakarta.servlet.http.HttpSession;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.memory.InMemoryChatMemory;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;


import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY;


@RestController
public class OpenAiChatController {
    private final ChatClient chatClient;

    private final InMemoryChatMemory chatMemory;

    private final CorporationService corporationService;

    OpenAiChatController(ChatClient.Builder chatClientBuilder, CorporationService corporationService){

        this.corporationService = corporationService;
        this.chatMemory = new InMemoryChatMemory();
        this.chatClient = chatClientBuilder
                .defaultSystem("""
                        You are a personalized AI assistant designed for customer support services.
                        Each corporation you work with provides a tailored list of frequently asked questions
                        (FAQs) with corresponding answers. Your primary task is to engage in conversations with
                        users, identify relevant questions from the provided FAQ list, and deliver precise answers
                        based on that information.
   	
                        During interactions, focus on recognizing and responding only to questions that match
                        the entries in the FAQ list. If a user poses a question outside the scope of the provided
                        FAQs, politely inquire if they would like to ask a different question or escalate their
                        inquiry to the human support team. If the user opts for escalation, guide them to click
                        the appropriate button to proceed with contacting customer support.
                        
                        The FAQs:
                        {FAQ}
\t""")
                .defaultAdvisors(
                        new PromptChatMemoryAdvisor(chatMemory))
                .build();
    }

    @GetMapping("{corp_name}/chat/{message}")
    String getResponseBack(@PathVariable String corp_name, @PathVariable String message, HttpSession session){ //Temporarily a String, not a good practice
        // Check if the corporation exists
        if (corporationService.findByName(corp_name).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Corporation not found");
        }

        String sessionId = session.getId();

        //TODO: FAQ retrieval logik
        String response = this.chatClient.prompt().system(s -> s.param("FAQ", "TODO"))
                .user(message)
                .advisors(a -> a.param(CHAT_MEMORY_CONVERSATION_ID_KEY, sessionId))
                .call().content();


        //TODO: Check if conversation history can be directly saved to the DB
        var memory = chatMemory.get(sessionId, Integer.MAX_VALUE);
        return response;

        //TODO: Verify if a conversation with the current session ID exists in the database, and update the session if necessary
    }
}

