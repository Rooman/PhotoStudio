package com.photostudio.service.impl;


import com.photostudio.dao.EmailTemplateDao;
import com.photostudio.dao.UserDao;
import com.photostudio.dao.jdbc.*;
import com.photostudio.dao.jdbc.testUtils.TestDataSource;
import com.photostudio.entity.user.User;
import com.photostudio.entity.user.UserRole;
import com.photostudio.exception.ChangeOrderStatusInvalidException;
import com.photostudio.service.MailService;
import com.photostudio.service.OrderStatusService;
import com.photostudio.service.UserService;
import com.photostudio.service.testUtils.MockMailSender;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DefaultOrderServiceITest {

    private static TestDataSource dataSource;
    private static DefaultOrderService orderService;

    @BeforeAll
    public static void before() throws SQLException, IOException {
        dataSource = new TestDataSource();
        JdbcDataSource jdbcDataSource = dataSource.init();
        JdbcOrderDao jdbcOrderDao = new JdbcOrderDao(jdbcDataSource);

        JdbcOrderStatusCachedDao jdbcOrderStatusCachedDao = new JdbcOrderStatusCachedDao(jdbcDataSource);
        OrderStatusService orderStatusService = new DefaultOrderStatusService(jdbcOrderStatusCachedDao);

        MockMailSender mockMailSender = new MockMailSender(dataSource);
        UserDao userDao = new JdbcUserDao(jdbcDataSource);
        UserService userService = new DefaultUserService(userDao);

        EmailTemplateDao emailTemplateDao = new JdbcEmailTemplateCachedDao(jdbcDataSource);
        MailService mailService = new DefaultMailService(mockMailSender, userService, emailTemplateDao);

        orderService = new DefaultOrderService(jdbcOrderDao, orderStatusService, mailService);
    }

    @BeforeEach
    public void beforeEach() throws SQLException, IOException {
        dataSource.execUpdate("DELETE FROM TestSentMails;");
        dataSource.runScript("db/data_change_status.sql");
    }

    @Test
    public void testToViewAndSelectByAdmin() throws SQLException {

        User user = new User();
        user.setUserRole(UserRole.ADMIN);
        user.setEmail("admin@test.com");
        assertDoesNotThrow(() -> {
            orderService.moveStatusForward(1, user);
        });
        //after
        int statusOrder = dataSource.getResult("SELECT statusId FROM Orders WHERE id = 1");
        assertEquals(2, statusOrder);

        int cntMails = dataSource.getResult("SELECT COUNT(*) FROM TestSentMails");
        assertEquals(1, cntMails);

        String mailTo = dataSource.getString("SELECT mailto FROM TestSentMails");
        assertEquals("user2@test.com", mailTo);

        String subject = dataSource.getString("SELECT subject FROM TestSentMails");
        assertEquals("Your order 1 is ready.", subject);
    }

    @Test
    public void testToViewAndSelectWithoutPhoto() throws SQLException {

        User user = new User();
        user.setUserRole(UserRole.ADMIN);
        user.setEmail("admin@test.com");
        assertThrows(ChangeOrderStatusInvalidException.class, () -> {
            orderService.moveStatusForward(5, user);
        });
        //after
        int statusOrder = dataSource.getResult("SELECT statusId FROM Orders WHERE id = 5");
        assertEquals(1, statusOrder);

        int cntMails = dataSource.getResult("SELECT COUNT(*) FROM TestSentMails");
        assertEquals(0, cntMails);
    }


    @Test
    public void testToViewAndSelectByUser() throws SQLException {

        User user = new User();
        user.setUserRole(UserRole.USER);
        user.setEmail("user2@test.com");
        user.setLangId(2);

        //after
        assertThrows(ChangeOrderStatusInvalidException.class, () -> {
            orderService.moveStatusForward(1, user);
        });

        int statusOrder = dataSource.getResult("SELECT statusId FROM Orders WHERE id = 1");
        assertEquals(1, statusOrder);

        int cntMails = dataSource.getResult("SELECT COUNT(*) FROM TestSentMails");
        assertEquals(0, cntMails);

    }

    @Test
    public void testToSelectedByAdmin() throws SQLException {

        User user = new User();
        user.setUserRole(UserRole.ADMIN);
        user.setEmail("admin@test.com");

        assertThrows(ChangeOrderStatusInvalidException.class, () -> {
            orderService.moveStatusForward(2, user);
        });

        int statusOrder = dataSource.getResult("SELECT statusId FROM Orders WHERE id = 2");
        assertEquals(2, statusOrder);

        int cntMails = dataSource.getResult("SELECT COUNT(*) FROM TestSentMails");
        assertEquals(0, cntMails);

    }

    @Test
    public void testToSelectedByUser() throws SQLException {

        User user = new User();
        user.setUserRole(UserRole.USER);
        user.setEmail("user2@test.com");
        user.setLangId(2);

        assertDoesNotThrow(() -> {
            orderService.moveStatusForward(2, user);
        });

        int statusOrder = dataSource.getResult("SELECT statusId FROM Orders WHERE id = 2");
        assertEquals(3, statusOrder);

        int cntMails = dataSource.getResult("SELECT COUNT(*) FROM TestSentMails");
        assertEquals(1, cntMails);

        String mailTo = dataSource.getString("SELECT mailto FROM TestSentMails");
        assertEquals("admin@test.com", mailTo);

        String subject = dataSource.getString("SELECT subject FROM TestSentMails");
        assertEquals("User user2@test.com has selected photo for order 2", subject);
    }

    @Test
    public void testToSelectedWithoutSelectedPhoto() throws SQLException {

        User user = new User();
        user.setUserRole(UserRole.USER);
        user.setEmail("user2@test.com");
        user.setLangId(2);

        assertThrows(ChangeOrderStatusInvalidException.class, () -> {
            orderService.moveStatusForward(6, user);
        });

        int statusOrder = dataSource.getResult("SELECT statusId FROM Orders WHERE id = 6");
        assertEquals(2, statusOrder);

        int cntMails = dataSource.getResult("SELECT COUNT(*) FROM TestSentMails");
        assertEquals(0, cntMails);
    }

    @Test
    public void testToReadyByAdmin() throws SQLException {

        User user = new User();
        user.setUserRole(UserRole.ADMIN);
        user.setEmail("admin@test.com");

        assertDoesNotThrow(() -> {
            orderService.moveStatusForward(3, user);
        });


        int statusOrder = dataSource.getResult("SELECT statusId FROM Orders WHERE id = 3");
        assertEquals(4, statusOrder);

        int cntMails = dataSource.getResult("SELECT COUNT(*) FROM TestSentMails");
        assertEquals(1, cntMails);

        String mailTo = dataSource.getString("SELECT mailto FROM TestSentMails");
        assertEquals("user2@test.com", mailTo);

        String subject = dataSource.getString("SELECT subject FROM TestSentMails");
        assertEquals("Your Order 3 is ready.", subject);

    }

    @Test
    public void testToReadyByAdminWithoutReadyPhoto() throws SQLException {

        User user = new User();
        user.setUserRole(UserRole.ADMIN);
        user.setEmail("admin@test.com");

        assertThrows(ChangeOrderStatusInvalidException.class, () -> {
            orderService.moveStatusForward(7, user);
        });


        int statusOrder = dataSource.getResult("SELECT statusId FROM Orders WHERE id = 7");
        assertEquals(3, statusOrder);

        int cntMails = dataSource.getResult("SELECT COUNT(*) FROM TestSentMails");
        assertEquals(0, cntMails);
    }

    @Test
    public void testToReadyByUser() throws SQLException {

        User user = new User();
        user.setUserRole(UserRole.USER);
        user.setEmail("user2@test.com");
        user.setLangId(2);

        assertThrows(ChangeOrderStatusInvalidException.class, () -> {
            orderService.moveStatusForward(3, user);
        });

        int statusOrder = dataSource.getResult("SELECT statusId FROM Orders WHERE id = 3");
        assertEquals(3, statusOrder);

        int cntMails = dataSource.getResult("SELECT COUNT(*) FROM TestSentMails");
        assertEquals(0, cntMails);

    }

    @Test
    public void testNextStatusFromReadyByAdmin() throws SQLException {

        User user = new User();
        user.setUserRole(UserRole.ADMIN);
        user.setEmail("admin@test.com");

        assertThrows(ChangeOrderStatusInvalidException.class, () -> {
            orderService.moveStatusForward(4, user);
        });

        int statusOrder = dataSource.getResult("SELECT statusId FROM Orders WHERE id = 4");
        assertEquals(4, statusOrder);

        int cntMails = dataSource.getResult("SELECT COUNT(*) FROM TestSentMails");
        assertEquals(0, cntMails);

    }

    @Test
    public void testNextStatusFromReadyByUser() throws SQLException {

        User user = new User();
        user.setUserRole(UserRole.USER);
        user.setEmail("user2@test.com");
        user.setLangId(2);

        assertThrows(ChangeOrderStatusInvalidException.class, () -> {
            orderService.moveStatusForward(4, user);
        });

        int statusOrder = dataSource.getResult("SELECT statusId FROM Orders WHERE id = 4");
        assertEquals(4, statusOrder);

        int cntMails = dataSource.getResult("SELECT COUNT(*) FROM TestSentMails");
        assertEquals(0, cntMails);

    }

    @Test
    public void testPreviousStatusFromReadyByAdmin() throws SQLException {
        User user = new User();
        user.setUserRole(UserRole.ADMIN);
        user.setEmail("admin@test.com");

        assertThrows(ChangeOrderStatusInvalidException.class, () -> {
            orderService.moveStatusBack(4, user);
        });

        int statusOrder = dataSource.getResult("SELECT statusId FROM Orders WHERE id = 4");
        assertEquals(4, statusOrder);

        int cntMails = dataSource.getResult("SELECT COUNT(*) FROM TestSentMails");
        assertEquals(0, cntMails);

    }

    @Test
    public void testPreviousStatusFromReadyByUser() throws SQLException {
        User user = new User();
        user.setUserRole(UserRole.USER);
        user.setEmail("user2@test.com");
        user.setLangId(2);

        assertDoesNotThrow(() -> {
            orderService.moveStatusBack(4, user);
        });

        int statusOrder = dataSource.getResult("SELECT statusId FROM Orders WHERE id = 4");
        assertEquals(3, statusOrder);

        int cntMails = dataSource.getResult("SELECT COUNT(*) FROM TestSentMails");
        assertEquals(1, cntMails);

        String mailTo = dataSource.getString("SELECT mailto FROM TestSentMails");
        assertEquals("admin@test.com", mailTo);

        String subject = dataSource.getString("SELECT subject FROM TestSentMails");
        assertEquals("User user2@test.com has selected photo for order 4", subject);

    }

    @Test
    public void testPreviousStatusFromReadyWithotSelectedPhoto() throws SQLException {
        User user = new User();
        user.setUserRole(UserRole.USER);
        user.setEmail("user2@test.com");
        user.setLangId(2);

        assertThrows(ChangeOrderStatusInvalidException.class, () -> {
            orderService.moveStatusBack(8, user);
        });

        int statusOrder = dataSource.getResult("SELECT statusId FROM Orders WHERE id = 4");
        assertEquals(4, statusOrder);

        int cntMails = dataSource.getResult("SELECT COUNT(*) FROM TestSentMails");
        assertEquals(0, cntMails);

    }

    @Test
    public void testPreviousStatusFromSelectedByAdmin() throws SQLException {
        User user = new User();
        user.setUserRole(UserRole.ADMIN);
        user.setEmail("admin@test.com");

        assertThrows(ChangeOrderStatusInvalidException.class, () -> {
            orderService.moveStatusBack(3, user);
        });

        int statusOrder = dataSource.getResult("SELECT statusId FROM Orders WHERE id = 3");
        assertEquals(3, statusOrder);

        int cntMails = dataSource.getResult("SELECT COUNT(*) FROM TestSentMails");
        assertEquals(0, cntMails);

    }

    @Test
    public void testPreviousStatusFromSelectedByUser() throws SQLException {

        User user = new User();
        user.setUserRole(UserRole.USER);
        user.setEmail("user@test.com");
        user.setLangId(2);

        assertThrows(ChangeOrderStatusInvalidException.class, () -> {
            orderService.moveStatusBack(3, user);
        });

        int statusOrder = dataSource.getResult("SELECT statusId FROM Orders WHERE id = 3");
        assertEquals(3, statusOrder);

        int cntMails = dataSource.getResult("SELECT COUNT(*) FROM TestSentMails");
        assertEquals(0, cntMails);

    }

    @Test
    public void testPreviousStatusFromViewAndSelectByAdmin() throws SQLException {

        User user = new User();
        user.setUserRole(UserRole.ADMIN);
        user.setEmail("test@test.com");

        assertThrows(ChangeOrderStatusInvalidException.class, () -> {
            orderService.moveStatusBack(2, user);
        });

        int statusOrder = dataSource.getResult("SELECT statusId FROM Orders WHERE id = 2");
        assertEquals(2, statusOrder);

        int cntMails = dataSource.getResult("SELECT COUNT(*) FROM TestSentMails");
        assertEquals(0, cntMails);

    }

    @Test
    public void testPreviousStatusFromViewAndSelectByUser() throws SQLException {

        User user = new User();
        user.setUserRole(UserRole.USER);
        user.setEmail("user@test.com");
        user.setLangId(2);

        assertThrows(ChangeOrderStatusInvalidException.class, () -> {
            orderService.moveStatusBack(2, user);
        });

        int statusOrder = dataSource.getResult("SELECT statusId FROM Orders WHERE id = 2");
        assertEquals(2, statusOrder);

        int cntMails = dataSource.getResult("SELECT COUNT(*) FROM TestSentMails");
        assertEquals(0, cntMails);

    }


    @AfterAll
    public static void after() throws SQLException {
        dataSource.close();
    }
}
