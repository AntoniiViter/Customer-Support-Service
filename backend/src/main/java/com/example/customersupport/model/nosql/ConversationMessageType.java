package com.example.customersupport.model.nosql;

import lombok.Getter;

@Getter
public enum ConversationMessageType {
    USER("user"),
    ASSISTANT("assistant");

    private final String type;

    ConversationMessageType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type;
    }

    // fromValue method to convert a string to a ConversationMessageType enum
    public static ConversationMessageType fromValue(String value) {
        for (ConversationMessageType messageType : ConversationMessageType.values()) {
            if (messageType.getType().equalsIgnoreCase(value)) {
                return messageType;
            }
        }
        throw new IllegalArgumentException("Unknown message type: " + value);
    }
}