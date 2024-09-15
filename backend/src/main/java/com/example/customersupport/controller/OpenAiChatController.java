package com.example.customersupport.controller;
import com.example.customersupport.model.relational.Client;
import com.example.customersupport.model.relational.Corporation;
import com.example.customersupport.service.ClientService;
import com.example.customersupport.service.ConversationService;
import com.example.customersupport.service.CorporationService;
import com.example.customersupport.service.OpenAiChatService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@RestController
public class OpenAiChatController {

    private final OpenAiChatService openAiChatService;
    private final ConversationService conversationService;
    private final CorporationService corporationService;
    private final ClientService clientService;

    public OpenAiChatController(OpenAiChatService openAiChatService, ConversationService conversationService, CorporationService corporationService, ClientService clientService) {
        this.openAiChatService = openAiChatService;
        this.conversationService = conversationService;
        this.corporationService = corporationService;
        this.clientService = clientService;
    }

    @GetMapping("/{corpName}/support")
    public ResponseEntity<Flux<String>> getResponseBack(@PathVariable String corpName,
                                                        @RequestParam String message,
                                                        @RequestParam(name = "email", required = false) String requestParamClientEmail,
                                                        HttpServletRequest request) {


        // Check if the corporation exists
        Optional<Corporation> optionalCorporation = corporationService.getCorporationByCorpName(corpName);

        if (optionalCorporation.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Corporation not found");
        }
        // Validate client email if provided
        if (requestParamClientEmail != null && !requestParamClientEmail.isEmpty()) {
            if (!isValidEmail(requestParamClientEmail)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid email format");
            }
        }

        HttpSession session = request.getSession(true);

        String sessionCorpName = (String) session.getAttribute("corpName");
        if(sessionCorpName == null) {
            session.setAttribute("corpName", corpName);
        } else if(!corpName.equals(sessionCorpName)){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "You must end the conversation with the previous corporation: " + sessionCorpName);
        }

        String conversationId = (String) session.getAttribute("conversationId");

        String sessionClientEmail = (String) session.getAttribute("email");
        String dbClientEmail;

        if (conversationId == null) {
            if (requestParamClientEmail != null) {
                session.setAttribute("email", requestParamClientEmail);
            }
            conversationId = conversationService.openConversation(requestParamClientEmail, corpName);
            session.setAttribute("conversationId", conversationId);
        } else {
            if (requestParamClientEmail == null) {
                dbClientEmail = clientService.findClientByConversationId(conversationId).map(Client::getEmail).orElse(null);
                if (dbClientEmail != null && !dbClientEmail.equals(sessionClientEmail) && !"anonymous@example.com".equals(dbClientEmail)) {
                    session.setAttribute("email", dbClientEmail);
                }
            } else {
                if (!requestParamClientEmail.equals(sessionClientEmail)) {
                    session.setAttribute("email", requestParamClientEmail);
                    conversationService.changeConversationClient(conversationId, requestParamClientEmail, corpName);
                } else {
                    dbClientEmail = clientService.findClientByConversationId(conversationId).map(Client::getEmail).orElse(null);
                    if (dbClientEmail != null && !dbClientEmail.equals(sessionClientEmail)) {
                        session.setAttribute("email", dbClientEmail);
                    }
                }
            }
        }

        return ResponseEntity.ok(openAiChatService.getResponseFromChat(corpName, message, session.getId(), conversationId, (String) session.getAttribute("email")));
    }


    @GetMapping("/{corpName}/support/restart")
    public ResponseEntity<String> refreshSession(HttpServletRequest request) {

        HttpSession session = request.getSession(true);
        // Retrieve the current SecurityContext from the session
        SecurityContext securityContext = (SecurityContext) session.getAttribute("SPRING_SECURITY_CONTEXT");

        // Invalidate the current session and create a new one
        session.invalidate();
        HttpSession newSession = request.getSession(true); // true indicates creating a new session

        // Transfer the SecurityContext to the new session
        if (securityContext != null) {
            newSession.setAttribute("SPRING_SECURITY_CONTEXT", securityContext);
        }


        return ResponseEntity.ok("Session was successfully refreshed");
    }


    // Utility method to validate email format
    private boolean isValidEmail(String email) {
        // Regular expression for validating email
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        if (email.equals("anonymous@example.com")) {
            return false;
        }
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}

