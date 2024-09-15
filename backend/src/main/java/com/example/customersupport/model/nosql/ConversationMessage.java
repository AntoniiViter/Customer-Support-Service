package com.example.customersupport.model.nosql;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Field;


@Data
@NoArgsConstructor
@Getter
@Setter
public class ConversationMessage {

    @Field(name = "content")
    private String content;  // The actual message content

    @Field(name = "sender")
    private String sender;  // String indicating who sent the message (User or Assistant)

    public ConversationMessage(String content, String sender) {
        this.sender = sender;
        this.content = content;
    }

    /*
    //// Method to retrieve the content or modelResponse based on the sender
    //private String processContent(String content, ConversationMessageType sender) {
    //    if (sender == ConversationMessageType.ASSISTANT) {
    //        // Try to parse the content as JSON and extract the "modelResponse" field
    //        try {
    //            ObjectMapper objectMapper = new ObjectMapper();
    //            JsonNode rootNode = objectMapper.readTree(content);
    //            return rootNode.path("modelResponse").asText();  // Return only the "modelResponse" part
    //        } catch (Exception e) {
    //            // If parsing fails, return the full content as a fallback
    //            e.printStackTrace();
    //            return content;
    //        }
    //    } else {
    //        // If the sender is a user, return the full content
    //        return content;
    //    }
    //}

     */
}
