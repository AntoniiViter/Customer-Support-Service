package com.example.customersupport.model.nosql;

import lombok.Getter;

@Getter
public enum ConversationStatus {
    ACTIVE("active"),
    ESCALATED("escalated"),
    CLOSED("closed"),
    FAILED("failed");

    private final String status;

    ConversationStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return status;
    }

    // fromValue method to convert a string to a ConversationStatus enum
    public static ConversationStatus fromValue(String value) {
        for (ConversationStatus status : ConversationStatus.values()) {
            if (status.getStatus().equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown status: " + value);
    }
}