package com.example.customersupport.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.stereotype.Service;
import org.springframework.session.Session;

import java.time.Duration;
import java.util.function.Function;

import static com.example.customersupport.model.nosql.ConversationStatus.*;

@Service
public class ChatFunctionService implements Function<ChatFunctionService.Request, ChatFunctionService.Response>  {

    // Define necessary services (e.g., conversationService)
    @Autowired
    private ConversationService conversationService;

    @Autowired
    private FindByIndexNameSessionRepository redisSessionRepository;


    public enum State {
        SUCCESS,
        ESCALATION,
        FAILURE,
    }

    public record Request(State state, String conversationId, String sessionId, String newClientEmail, String corpName, String headline) {}
    public record Response() {}

    @Override
    public Response apply(Request request) {
        // Check if there is a new email provided
        if (request.newClientEmail != null && !request.newClientEmail.isEmpty()) {
            // Logic to change the email for the conversation
            conversationService.changeConversationClient(request.conversationId, request.newClientEmail, request.corpName);
        }

        if (request.headline != null && !request.headline.isEmpty()) {
            // Logic to change the headline for the conversation
            conversationService.setHeadline(request.conversationId, request.headline);
        }

        if(request.state == null) {
            return new Response();
        }

        switch (request.state) {
            case SUCCESS -> {
                conversationService.updateConversationState(request.conversationId, CLOSED.getStatus());
                refreshSession(request.sessionId);
            }
            case ESCALATION -> {
                conversationService.updateConversationState(request.conversationId, ESCALATED.getStatus());
                refreshSession(request.sessionId);
            }
            case FAILURE -> {
                conversationService.updateConversationState(request.conversationId, FAILED.getStatus());
                refreshSession(request.sessionId);
            }
        }

        return new Response();
    }

    public void refreshSession(String sessionId) {
        Session session = redisSessionRepository.findById(sessionId);
        if(!session.isExpired()) {
            session.setMaxInactiveInterval(Duration.ofSeconds(5));
        }
        if(!session.isExpired()) {
            redisSessionRepository.save(session);
        }
    }
}
