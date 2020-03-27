package com.photostudio.dao.jdbc;


import com.photostudio.dao.EmailTemplateDao;
import com.photostudio.dao.jdbc.entity.EmailTemplateRow;
import com.photostudio.dao.jdbc.mapper.EmailTemplateRowMapper;
import com.photostudio.dao.jdbc.mapper.LanguageRowMapper;
import com.photostudio.entity.order.OrderStatus;
import com.photostudio.entity.user.UserLanguage;
import com.photostudio.service.entity.EmailTemplate;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

@Slf4j
public class JdbcEmailTemplateCachedDao implements EmailTemplateDao {
    List<EmailTemplateRow> templateRows;
    private static final String GET_ALL_TEMPLATES = "SELECT t.langId, " +
            "s.statusName, " +
            "t.subject, " +
            "t.body " +
            "FROM EmailTemplates t " +
            "JOIN OrderStatus s ON (t.orderStatusId = s.id)";

    public JdbcEmailTemplateCachedDao(DataSource dataSource) {
        load(dataSource);
    }

    @Override
    public EmailTemplate getByLangAndStatus(int langId, OrderStatus orderStatus) {
        return templateRows.stream()
                .filter(emailTemplateRow -> langId == emailTemplateRow.getLangId() && orderStatus == emailTemplateRow.getOrderStatus())
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Email template is not found")).getEmailTemplate();
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
}
