package com.photostudio.entity.email;

import java.util.Arrays;

public enum  MessageType {
    NEW_PASSWORD(1, "NEW PASSWORD"),
    RESET_PASSWORD(2, "RESET PASSWORD");

    private int messageId;
    private String nameMessageType;

    MessageType(int messageId, String nameMessageType) {
        this.messageId = messageId;
        this.nameMessageType = nameMessageType;
    }

    public int getMessageId() {
        return messageId;
    }

    public String getNameMessageType() {
        return nameMessageType;
    }

    public static MessageType getByMessageType(String messageTypeName) {

        return Arrays.stream(MessageType.values())
                .filter(messageType -> messageType.nameMessageType.equalsIgnoreCase(messageTypeName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("MessageTpe name is not correct!"));
    }

    public static MessageType getById(int id) {
        return Arrays.stream(MessageType.values())
                .filter(messageType -> (messageType.messageId == id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("MessageType id is not correct!"));
    }
}
