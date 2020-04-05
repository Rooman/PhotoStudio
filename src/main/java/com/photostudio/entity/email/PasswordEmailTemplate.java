package com.photostudio.entity.email;

import lombok.Data;

@Data
public class PasswordEmailTemplate {
    private int id;
    private int langId;
    private String subject;
    private String body;
    private MessageType messageType;
}
