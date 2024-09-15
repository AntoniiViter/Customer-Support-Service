package com.example.customersupport.config;

import com.example.customersupport.service.ChatFunctionService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;

import java.util.function.Function;

@Configuration
public class ChatFunctionConfiguration {
    @Description("Conversation management function")
    @Bean
    public Function<ChatFunctionService.Request, ChatFunctionService.Response> openAiChatFunction() {
        return new ChatFunctionService();
    }
}
