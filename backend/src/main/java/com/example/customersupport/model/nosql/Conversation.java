package com.example.customersupport.model.nosql;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;


import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;



@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "conversations")
public class Conversation {

    @Id
    private String id;  // Unique identifier for the conversation

    @Field(name = "messages")
    private List<ConversationMessage> messages;  // List of messages exchanged during the conversation

    @Field(name = "created_at")
    private LocalDateTime createdAt;  // Timestamp for when the conversation started

    @Field(name = "status")
    private String status;  // Status of the conversation (e.g., "active", "escalated", "closed")

    @NotNull
    @Field(name = "client_email")
    private String clientEmail;

    @NotNull
    @Field(name = "corp_name")
    private String corpName;

    @Field(name = "headline")
    private String headline;  // Headline describing the conversation
}