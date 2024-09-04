package com.example.customersupport.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class OpenAiChatController {
    private final ChatClient chatClient;

    OpenAiChatController(ChatClient.Builder chatClientBuilder){
        this.chatClient= chatClientBuilder
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
                        new PromptChatMemoryAdvisor(new InMemoryChatMemory())
                )
                .build();
    }

    @GetMapping("chat/{message}")
    String getResponseBack(@PathVariable String message){ //Temporarily a String, not a good practice
        return this.chatClient.prompt().system(s -> s.param("FAQ", "TODO")).user(message).call().content();
    }
}

