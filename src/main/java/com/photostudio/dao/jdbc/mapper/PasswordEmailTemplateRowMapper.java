package com.photostudio.dao.jdbc.mapper;

import com.photostudio.entity.email.MessageType;
import com.photostudio.entity.email.PasswordEmailTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PasswordEmailTemplateRowMapper {
    public PasswordEmailTemplate mapRow(ResultSet resultSet) throws SQLException {
        PasswordEmailTemplate passwordEmailTemplate = new PasswordEmailTemplate();
        passwordEmailTemplate.setId(resultSet.getInt("id"));
        passwordEmailTemplate.setLangId(resultSet.getInt("langId"));
        passwordEmailTemplate.setSubject(resultSet.getString("subject"));
        passwordEmailTemplate.setBody(resultSet.getString("body"));
        passwordEmailTemplate.setMessageType(MessageType.getByMessageType(resultSet.getString("messageType")));
        return passwordEmailTemplate;
    }
}
