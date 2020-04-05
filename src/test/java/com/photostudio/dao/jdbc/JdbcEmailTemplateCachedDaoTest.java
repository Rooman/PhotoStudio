package com.photostudio.dao.jdbc;

import com.photostudio.dao.jdbc.testUtils.TestDataSource;
import com.photostudio.entity.email.MessageType;
import com.photostudio.entity.email.PasswordEmailTemplate;
import com.photostudio.entity.order.OrderStatus;
import com.photostudio.service.entity.EmailTemplate;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;


class JdbcEmailTemplateCachedDaoTest {
    private static TestDataSource dataSource = new TestDataSource();
    private static JdbcEmailTemplateCachedDao emailTemplateDao;
    private static JdbcDataSource jdbcDataSource;

    @BeforeAll
    public static void beforeAll() throws IOException, SQLException {
        jdbcDataSource = dataSource.init();
        emailTemplateDao = new JdbcEmailTemplateCachedDao(jdbcDataSource);
    }

    @Test
    void testGetByLangAndStatus() {
        EmailTemplate emailTemplate = emailTemplateDao.getByLangAndStatus(1, OrderStatus.VIEW_AND_SELECT);
        assertEquals("Ihre Bestellung 1 ist fertig.", emailTemplate.generateHeader(1));

        emailTemplate = emailTemplateDao.getByLangAndStatus(2, OrderStatus.VIEW_AND_SELECT);
        assertEquals("Your order 1 is ready.", emailTemplate.generateHeader(1));

        assertThrows(Exception.class, () -> emailTemplateDao.getByLangAndStatus(5, OrderStatus.VIEW_AND_SELECT));
    }

    @Test
    void testLoad() {
        assertDoesNotThrow(() -> emailTemplateDao.load(jdbcDataSource));
        assertEquals(9, emailTemplateDao.templateRows.size());
    }

    @Test
    void testGetPasswordEmailTemplateByLangIdAndMessageType() {
        //prepare
        String expectedSubject = "Miari Fotografie Reset password";
        String expectedBody = "Ihr neues Passwort lautet <password>";

        //when
        PasswordEmailTemplate actualSubjectENPasswordEmailTemplate = emailTemplateDao
                .getPasswordEmailTemplateByLangIdAndMessageType(2, MessageType.RESET_PASSWORD);
        PasswordEmailTemplate actualBodyDEPasswordEmailTemplate = emailTemplateDao
                .getPasswordEmailTemplateByLangIdAndMessageType(1, MessageType.RESET_PASSWORD);

        //then
        assertEquals(expectedBody, actualBodyDEPasswordEmailTemplate.getBody());
        assertEquals(expectedSubject, actualSubjectENPasswordEmailTemplate.getSubject());
    }

    @AfterAll
    public static void closeConnection() throws SQLException {
        dataSource.close();
    }
}