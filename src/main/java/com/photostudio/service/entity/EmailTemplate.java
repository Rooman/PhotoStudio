package com.photostudio.service.entity;


public class EmailTemplate {
    private String header;
    private String body;

    public EmailTemplate(String header, String body) {
        this.header = header;
        this.body = body;
    }

    public String generateHeader(Object... params) {
        return String.format(header, params);
    }

    public String generateBody(Object... params) {
        return String.format(body, params);
    }
}
