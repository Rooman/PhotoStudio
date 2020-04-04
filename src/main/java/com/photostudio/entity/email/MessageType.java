package com.photostudio.entity.email;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum MessageType {
    NEW_PASSWORD(1, "NEW PASSWORD"),
    RESET_PASSWORD(2, "RESET PASSWORD");

    private int messageId;
    private String nameMessageType;

    public static MessageType getByMessageType(String messageTypeName) {
        return Arrays.stream(MessageType.values())
                .filter(messageType -> messageType.nameMessageType.equalsIgnoreCase(messageTypeName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("MessageTpe name " + messageTypeName + " is not correct!"));
    }

    public static MessageType getById(int id) {
        return Arrays.stream(MessageType.values())
                .filter(messageType -> (messageType.messageId == id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("MessageType id " + id + " is not correct!"));
    }
}
