package com.photostudio.dao.jdbc;


import com.photostudio.dao.EmailTemplateDao;
import com.photostudio.dao.jdbc.entity.EmailTemplateRow;
import com.photostudio.dao.jdbc.mapper.EmailTemplateRowMapper;
import com.photostudio.dao.jdbc.mapper.LanguageRowMapper;
import com.photostudio.dao.jdbc.mapper.PasswordEmailTemplateRowMapper;
import com.photostudio.entity.email.MessageType;
import com.photostudio.entity.email.PasswordEmailTemplate;
import com.photostudio.entity.order.OrderStatus;
import com.photostudio.entity.user.UserLanguage;
import com.photostudio.service.entity.EmailTemplate;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

@Slf4j
public class JdbcEmailTemplateCachedDao implements EmailTemplateDao {
    private static final PasswordEmailTemplateRowMapper PASSWORD_EMAIL_TEMPLATE_ROW_MAPPER = new PasswordEmailTemplateRowMapper();
    List<EmailTemplateRow> templateRows;
    private static final String GET_ALL_TEMPLATES = "SELECT t.langId, " +
            "s.statusName, " +
            "t.subject, " +
            "t.body " +
            "FROM EmailTemplates t " +
            "JOIN OrderStatus s ON (t.orderStatusId = s.id)";

    private static final String GET_ALL_PASSWORD_EMAIL_TEMPLATES = "SELECT id, " +
            "langId, " +
            "subject, " +
            "body, " +
            "messageType " +
            "FROM " +
            "PasswordEmailTemplate;";

    private final DataSource dataSource;
    private List<PasswordEmailTemplate> passwordEmailTemplateCache;

    public JdbcEmailTemplateCachedDao(DataSource dataSource) {
        this.dataSource = dataSource;
        load(dataSource);
        setPasswordEmailTemplates();
    }

    @Override
    public EmailTemplate getByLangAndStatus(int langId, OrderStatus orderStatus) {
        return templateRows.stream()
                .filter(emailTemplateRow -> langId == emailTemplateRow.getLangId() && orderStatus == emailTemplateRow.getOrderStatus())
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Email template is not found")).getEmailTemplate();
    }

    @Override
    public PasswordEmailTemplate getPasswordEmailTemplateByLangIdAndMessageType(int langId, MessageType messageType) {
        Optional<PasswordEmailTemplate> optionalPasswordEmailTemplate = passwordEmailTemplateCache.stream()
                .filter(passwordEmailTemplate -> passwordEmailTemplate.getMessageType() == messageType)
                .filter(passwordEmailTemplate -> passwordEmailTemplate.getLangId() == langId)
                .findFirst();
        if (optionalPasswordEmailTemplate.isPresent()) {
            return optionalPasswordEmailTemplate.get();
        } else {
            log.info("PasswordEmailTemplate with messageType {} and langId {} does not exist", messageType, langId);
            throw new RuntimeException("PasswordEmailTemplate with messageType " + messageType + " and langId " + langId + " does not exist");
        }
    }

    void load(DataSource dataSource) {
        log.info("Load templates cache");
        List<EmailTemplateRow> listTemplateRows = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(GET_ALL_TEMPLATES)) {
            while (resultSet.next()) {
                EmailTemplateRow emailTemplateRow = EmailTemplateRowMapper.mapRow(resultSet);
                listTemplateRows.add(emailTemplateRow);
            }
            this.templateRows = Collections.unmodifiableList(listTemplateRows);
        } catch (SQLException e) {
            log.error("Get email templates cache error", e);
            throw new RuntimeException("Get email templates cache error", e);
        }
    }

    private void setPasswordEmailTemplates() {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL_PASSWORD_EMAIL_TEMPLATES);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            passwordEmailTemplateCache = new ArrayList<>();
            while (resultSet.next()) {
                passwordEmailTemplateCache.add(PASSWORD_EMAIL_TEMPLATE_ROW_MAPPER.mapRow(resultSet));
            }
        } catch (SQLException e) {
            log.info("Error setting passwordEmailTemplateCache", e);
            throw new RuntimeException("Error setting passwordEmailTemplateCache", e);
        }
    }
}
