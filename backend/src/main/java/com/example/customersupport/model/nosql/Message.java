package com.example.customersupport.model.nosql;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    private String sender;  // Sender of the message (e.g., "client" or "bot")

    private String content;  // Content of the message
}