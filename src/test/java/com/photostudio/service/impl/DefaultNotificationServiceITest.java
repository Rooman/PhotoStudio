package com.photostudio.service.impl;

import com.photostudio.dao.EmailTemplateDao;
import com.photostudio.dao.UserDao;
import com.photostudio.dao.UserLanguageDao;
import com.photostudio.dao.jdbc.JdbcEmailTemplateCachedDao;
import com.photostudio.dao.jdbc.JdbcUserDao;
import com.photostudio.dao.jdbc.JdbcUserLanguageCachedDao;
import com.photostudio.dao.jdbc.testUtils.TestDataSource;
import com.photostudio.entity.order.OrderStatus;
import com.photostudio.entity.user.User;
import com.photostudio.service.UserService;
import com.photostudio.service.testUtils.MockMailSender;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class DefaultNotificationServiceITest {
    private static TestDataSource dataSource = new TestDataSource();
    private static DefaultNotificationService defaultMailService;

    @BeforeAll
    public static void init() throws SQLException, IOException {
        JdbcDataSource jdbcDataSource = dataSource.init();
        dataSource.runScript("db/data_change_status.sql");

        MockMailSender mockMailSender = new MockMailSender(dataSource);
        UserLanguageDao userLanguageDao = new JdbcUserLanguageCachedDao(jdbcDataSource);
        UserDao userDao = new JdbcUserDao(jdbcDataSource);
        UserService userService = new DefaultUserService(userDao);
        EmailTemplateDao emailTemplateDao = new JdbcEmailTemplateCachedDao(jdbcDataSource);
        defaultMailService = new DefaultNotificationService(mockMailSender, userService, emailTemplateDao);
    }

    @BeforeEach
    public void setUp() throws SQLException {
        dataSource.execUpdate("DELETE FROM TestSentMails;");
    }

    @Test
    public void sendOnChangeStatusViewAndSelect() throws SQLException {
        User user = new User();
        user.setEmail("admin@test.com");
        user.setLangId(2);

        defaultMailService.sendOnChangeStatus(user, 1, OrderStatus.VIEW_AND_SELECT);
        int cntMails = dataSource.getResult("SELECT COUNT(*) FROM TestSentMails");
        assertEquals(1, cntMails);

        String mailTo = dataSource.getString("SELECT mailto FROM TestSentMails");
        assertEquals("user2@test.com", mailTo);

        String subject = dataSource.getString("SELECT subject FROM TestSentMails");
        assertEquals("Your order 1 is ready.", subject);
    }

    @Test
    public void sendOnChangeStatusSelected() throws SQLException {
        User user = new User();
        user.setEmail("user2@test.com");
        user.setLangId(2);

        defaultMailService.sendOnChangeStatus(user, 2, OrderStatus.SELECTED);
        int cntMails = dataSource.getResult("SELECT COUNT(*) FROM TestSentMails");
        assertEquals(1, cntMails);

        String mailTo = dataSource.getString("SELECT mailto FROM TestSentMails");
        assertEquals("admin@test.com", mailTo);

        String subject = dataSource.getString("SELECT subject FROM TestSentMails");
        assertEquals("User user2@test.com has selected photo for order 2", subject);
    }

    @Test
    public void sendOnChangeStatusReady() throws SQLException {
        User user = new User();
        user.setEmail("admin@test.com");
        user.setLangId(2);

        defaultMailService.sendOnChangeStatus(user, 3, OrderStatus.READY);
        int cntMails = dataSource.getResult("SELECT COUNT(*) FROM TestSentMails");
        assertEquals(1, cntMails);

        String mailTo = dataSource.getString("SELECT mailto FROM TestSentMails");
        assertEquals("user2@test.com", mailTo);

        String subject = dataSource.getString("SELECT subject FROM TestSentMails");
        assertEquals("Your Order 3 is ready.", subject);
    }


    @AfterAll
    public static void afterAll() throws SQLException {
        dataSource.close();
    }

}