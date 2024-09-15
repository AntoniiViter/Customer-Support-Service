package com.example.customersupport.controller;

import com.example.customersupport.model.nosql.Conversation;
import com.example.customersupport.service.ConversationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/{corpName}/conversations")
public class ConversationController {

    @Autowired
    private ConversationService conversationService;


    // GET: Retrieve a conversation by ID
    @GetMapping("/{conversationId}")
    public ResponseEntity<Conversation> getConversation(@PathVariable String conversationId) {
        Conversation conversation = conversationService.getConversationById(conversationId);
        return ResponseEntity.ok(conversation);
    }

    // PUT: Update an existing conversation
    @PutMapping("/{conversationId}")
    public ResponseEntity<Conversation> updateConversation(
            @PathVariable String conversationId,
            @RequestBody Conversation updatedConversation) {

        Conversation updated = conversationService.updateConversation(conversationId, updatedConversation);
        return ResponseEntity.ok(updated);
    }

    // DELETE: Remove a conversation from MongoDB and PostgreSQL
    @DeleteMapping("/{conversationId}")
    public ResponseEntity<String> deleteConversation(
            @PathVariable String conversationId) {

        conversationService.deleteConversation(conversationId);
        return ResponseEntity.ok("Conversation deleted successfully.");
    }

    // GET: Retrieve all conversations for a given client
    @GetMapping
    public ResponseEntity<List<Conversation>> getConversationsByClientEmail(@RequestParam String clientEmail) {
        List<Conversation> conversations = conversationService.getConversationsByClientEmail(clientEmail);
        return ResponseEntity.ok(conversations);
    }
}
