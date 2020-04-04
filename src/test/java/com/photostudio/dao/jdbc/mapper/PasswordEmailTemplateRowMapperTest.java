package com.photostudio.dao.jdbc.mapper;

import com.photostudio.entity.email.MessageType;
import com.photostudio.entity.email.PasswordEmailTemplate;
import org.junit.jupiter.api.Test;

import java.security.MessageDigest;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PasswordEmailTemplateRowMapperTest {

    @Test
    void testMapRow() throws SQLException {
        //prepare
        ResultSet mockResultSet = mock(ResultSet.class);

        String subject = "Miari Fotografie Reset password";
        String body = "Your new password is <password>";

        when(mockResultSet.getInt("id")).thenReturn(1);
        when(mockResultSet.getInt("langId")).thenReturn(2);
        when(mockResultSet.getString("subject")).thenReturn(subject);
        when(mockResultSet.getString("body")).thenReturn(body);
        when(mockResultSet.getString("messageType")).thenReturn("RESET PASSWORD");

        //when
        PasswordEmailTemplateRowMapper passwordEmailTemplateRowMapper = new PasswordEmailTemplateRowMapper();
        PasswordEmailTemplate passwordEmailTemplate = passwordEmailTemplateRowMapper.mapRow(mockResultSet);

        //then
        assertEquals(1, passwordEmailTemplate.getId());
        assertEquals(2, passwordEmailTemplate.getLangId());
        assertEquals(subject, passwordEmailTemplate.getSubject());
        assertEquals(body, passwordEmailTemplate.getBody());
        assertEquals(MessageType.RESET_PASSWORD, passwordEmailTemplate.getMessageType());
    }
}