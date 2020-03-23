package com.photostudio.service.impl;


import com.photostudio.dao.jdbc.JdbcOrderDao;
import com.photostudio.dao.jdbc.JdbcOrderStatusCachedDao;
import com.photostudio.dao.jdbc.testUtils.TestDataSource;
import com.photostudio.entity.user.User;
import com.photostudio.entity.user.UserRole;
import com.photostudio.exception.ChangeOrderStatusInvalidException;
import com.photostudio.service.MailService;
import com.photostudio.service.OrderStatusService;
import com.photostudio.service.testUtils.MockMailSender;
import com.photostudio.web.util.MailSender;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.*;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DefaultOrderServiceITest {
    private static TestDataSource dataSource = new TestDataSource();
    private static DefaultOrderService orderService;

    @BeforeAll
    public static void before() throws SQLException {
        JdbcDataSource jdbcDataSource = dataSource.init();
        dataSource.runScript("db/data.sql");
        JdbcOrderDao jdbcOrderDao = new JdbcOrderDao(jdbcDataSource);
        JdbcOrderStatusCachedDao jdbcOrderStatusCachedDao = new JdbcOrderStatusCachedDao(jdbcDataSource);
        OrderStatusService orderStatusService = new DefaultOrderStatusService(jdbcOrderStatusCachedDao);
        MockMailSender mockMailSender = new MockMailSender(dataSource);
        MailSender mailSender = (MailSender) mockMailSender;
        MailService mailService = new DefaultMailService(mailSender);
        orderService = new DefaultOrderService(jdbcOrderDao, null, orderStatusService, mailService);
    }

    @BeforeEach
    public void beforeEach() throws SQLException {
        dataSource.execUpdate("DELETE FROM TestSentMails;");
        dataSource.runScript("db/data_change_status.sql");
    }

    @Test
    public void testToViewAndSelectByAdmin() {

        User user = new User();
        user.setUserRole(UserRole.ADMIN);
        user.setEmail("test@test.com");
        assertDoesNotThrow(() -> {
            orderService.moveStatusForward(1, user);
        });
        //after
        int statusOrder = dataSource.getResult("SELECT statusId FROM Orders WHERE id = 1");
        assertEquals(2, statusOrder);

        int cntMails = dataSource.getResult("SELECT COUNT(*) FROM TestSentMails");
        assertEquals(1, cntMails);

        String mailTo = dataSource.getString("SELECT mailto FROM TestSentMails");
        assertEquals("test@test.com", mailTo);

        String subject = dataSource.getString("SELECT subject FROM TestSentMails");
        assertEquals("Order 1 is created", subject);
    }

    @Test
    public void testToViewAndSelectWithoutPhoto() {

        User user = new User();
        user.setUserRole(UserRole.ADMIN);
        user.setEmail("test@test.com");
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
    public void testToViewAndSelectByUser() {

        User user = new User();
        user.setUserRole(UserRole.USER);
        user.setEmail("user@test.com");
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
    public void testToSelectedByAdmin() {

        User user = new User();
        user.setUserRole(UserRole.ADMIN);
        user.setEmail("test@test.com");

        assertThrows(ChangeOrderStatusInvalidException.class, () -> {
            orderService.moveStatusForward(2, user);
        });

        int statusOrder = dataSource.getResult("SELECT statusId FROM Orders WHERE id = 2");
        assertEquals(2, statusOrder);

        int cntMails = dataSource.getResult("SELECT COUNT(*) FROM TestSentMails");
        assertEquals(0, cntMails);

    }

    @Test
    public void testToSelectedByUser() {

        User user = new User();
        user.setUserRole(UserRole.USER);
        user.setEmail("user@test.com");

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
        assertEquals("User user@test.com selected photo for order:2", subject);
    }

    @Test
    public void testToSelectedWithoutSelectedPhoto() {

        User user = new User();
        user.setUserRole(UserRole.USER);
        user.setEmail("user@test.com");

        assertThrows(ChangeOrderStatusInvalidException.class, () -> {
            orderService.moveStatusForward(6, user);
        });

        int statusOrder = dataSource.getResult("SELECT statusId FROM Orders WHERE id = 6");
        assertEquals(2, statusOrder);

        int cntMails = dataSource.getResult("SELECT COUNT(*) FROM TestSentMails");
        assertEquals(0, cntMails);
    }

    @Test
    public void testToReadyByAdmin() {

        User user = new User();
        user.setUserRole(UserRole.ADMIN);
        user.setEmail("test@test.com");

        assertDoesNotThrow(() -> {
            orderService.moveStatusForward(3, user);
        });


        int statusOrder = dataSource.getResult("SELECT statusId FROM Orders WHERE id = 3");
        assertEquals(4, statusOrder);

        int cntMails = dataSource.getResult("SELECT COUNT(*) FROM TestSentMails");
        assertEquals(1, cntMails);

        String mailTo = dataSource.getString("SELECT mailto FROM TestSentMails");
        assertEquals("test@test.com", mailTo);

        String subject = dataSource.getString("SELECT subject FROM TestSentMails");
        assertEquals("Order 3 is ready", subject);

    }

    @Test
    public void testToReadyByAdminWithoutReadyPhoto() {

        User user = new User();
        user.setUserRole(UserRole.ADMIN);
        user.setEmail("test@test.com");

        assertThrows(ChangeOrderStatusInvalidException.class, () -> {
            orderService.moveStatusForward(7, user);
        });


        int statusOrder = dataSource.getResult("SELECT statusId FROM Orders WHERE id = 7");
        assertEquals(3, statusOrder);

        int cntMails = dataSource.getResult("SELECT COUNT(*) FROM TestSentMails");
        assertEquals(0, cntMails);
    }

    @Test
    public void testToReadyByUser() {

        User user = new User();
        user.setUserRole(UserRole.USER);
        user.setEmail("user@test.com");

        assertThrows(ChangeOrderStatusInvalidException.class, () -> {
            orderService.moveStatusForward(3, user);
        });

        int statusOrder = dataSource.getResult("SELECT statusId FROM Orders WHERE id = 3");
        assertEquals(3, statusOrder);

        int cntMails = dataSource.getResult("SELECT COUNT(*) FROM TestSentMails");
        assertEquals(0, cntMails);

    }

    @Test
    public void testNextStatusFromReadyByAdmin() {

        User user = new User();
        user.setUserRole(UserRole.ADMIN);
        user.setEmail("test@test.com");

        assertThrows(ChangeOrderStatusInvalidException.class, () -> {
            orderService.moveStatusForward(4, user);
        });

        int statusOrder = dataSource.getResult("SELECT statusId FROM Orders WHERE id = 4");
        assertEquals(4, statusOrder);

        int cntMails = dataSource.getResult("SELECT COUNT(*) FROM TestSentMails");
        assertEquals(0, cntMails);

    }

    @Test
    public void testNextStatusFromReadyByUser() {

        User user = new User();
        user.setUserRole(UserRole.USER);
        user.setEmail("user@test.com");

        assertThrows(ChangeOrderStatusInvalidException.class, () -> {
            orderService.moveStatusForward(4, user);
        });

        int statusOrder = dataSource.getResult("SELECT statusId FROM Orders WHERE id = 4");
        assertEquals(4, statusOrder);

        int cntMails = dataSource.getResult("SELECT COUNT(*) FROM TestSentMails");
        assertEquals(0, cntMails);

    }

    @Test
    public void testPreviousStatusFromReadyByAdmin() {
        User user = new User();
        user.setUserRole(UserRole.ADMIN);
        user.setEmail("test@test.com");

        assertThrows(ChangeOrderStatusInvalidException.class, () -> {
            orderService.moveStatusBack(4, user);
        });

        int statusOrder = dataSource.getResult("SELECT statusId FROM Orders WHERE id = 4");
        assertEquals(4, statusOrder);

        int cntMails = dataSource.getResult("SELECT COUNT(*) FROM TestSentMails");
        assertEquals(0, cntMails);

    }

    @Test
    public void testPreviousStatusFromReadyByUser() {
        User user = new User();
        user.setUserRole(UserRole.USER);
        user.setEmail("user@test.com");

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
        assertEquals("User user@test.com selected photo for order:4", subject);

    }

    @Test
    public void testPreviousStatusFromReadyWithotSelectedPhoto() {
        User user = new User();
        user.setUserRole(UserRole.USER);
        user.setEmail("user@test.com");

        assertThrows(ChangeOrderStatusInvalidException.class, () -> {
            orderService.moveStatusBack(8, user);
        });

        int statusOrder = dataSource.getResult("SELECT statusId FROM Orders WHERE id = 4");
        assertEquals(4, statusOrder);

        int cntMails = dataSource.getResult("SELECT COUNT(*) FROM TestSentMails");
        assertEquals(0, cntMails);

    }

    @Test
    public void testPreviousStatusFromSelectedByAdmin() {
        User user = new User();
        user.setUserRole(UserRole.ADMIN);
        user.setEmail("test@test.com");

        assertThrows(ChangeOrderStatusInvalidException.class, () -> {
            orderService.moveStatusBack(3, user);
        });

        int statusOrder = dataSource.getResult("SELECT statusId FROM Orders WHERE id = 3");
        assertEquals(3, statusOrder);

        int cntMails = dataSource.getResult("SELECT COUNT(*) FROM TestSentMails");
        assertEquals(0, cntMails);

    }

    @Test
    public void testPreviousStatusFromSelectedByUser() {

        User user = new User();
        user.setUserRole(UserRole.USER);
        user.setEmail("user@test.com");

        assertThrows(ChangeOrderStatusInvalidException.class, () -> {
            orderService.moveStatusBack(3, user);
        });

        int statusOrder = dataSource.getResult("SELECT statusId FROM Orders WHERE id = 3");
        assertEquals(3, statusOrder);

        int cntMails = dataSource.getResult("SELECT COUNT(*) FROM TestSentMails");
        assertEquals(0, cntMails);

    }

    @Test
    public void testPreviousStatusFromViewAndSelectByAdmin() {

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
    public void testPreviousStatusFromViewAndSelectByUser() {

        User user = new User();
        user.setUserRole(UserRole.USER);
        user.setEmail("user@test.com");

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
