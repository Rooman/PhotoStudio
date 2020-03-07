package com.photostudio.service.impl;


import com.photostudio.dao.jdbc.JdbcOrderDao;
import com.photostudio.dao.jdbc.testUtils.TestDataSource;
import com.photostudio.entity.user.User;
import com.photostudio.entity.user.UserRole;
import com.photostudio.exception.ChangeOrderStatusInvalidException;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DefaultOrderServiceITest {
    private TestDataSource dataSource = new TestDataSource();
    private DefaultOrderService orderService = new DefaultOrderService();

    @BeforeEach
    public void before() throws SQLException, IOException {
        JdbcDataSource jdbcDataSource = dataSource.init();
        dataSource.runScript("db/data.sql");
        JdbcOrderDao jdbcOrderDao = new JdbcOrderDao(jdbcDataSource);
        orderService.setOrderDao(jdbcOrderDao);
    }

    @Test
    public void testToViewAndSelectByAdmin() {

        User user = new User();
        user.setUserRole(UserRole.ADMIN);
        assertDoesNotThrow(() -> {
            orderService.moveStatusForward(1, user);
        });
        //after
        int statusOrder = dataSource.getResult("SELECT statusId FROM Orders WHERE id = 1");
        assertEquals(2, statusOrder);
    }

    @Test
    public void testToViewAndSelectByUser() {

        User user = new User();
        user.setUserRole(UserRole.USER);

        //after
        assertThrows(ChangeOrderStatusInvalidException.class, () -> {
            orderService.moveStatusForward(1, user);
        });

        int statusOrder = dataSource.getResult("SELECT statusId FROM Orders WHERE id = 1");
        assertEquals(1, statusOrder);
    }

    @Test
    public void testToSelectedByAdmin() {

        User user = new User();
        user.setUserRole(UserRole.ADMIN);

        assertThrows(ChangeOrderStatusInvalidException.class, () -> {
            orderService.moveStatusForward(2, user);
        });

        int statusOrder = dataSource.getResult("SELECT statusId FROM Orders WHERE id = 2");
        assertEquals(2, statusOrder);
    }

    @Test
    public void testToSelectedByUser() {

        User user = new User();
        user.setUserRole(UserRole.USER);

        assertDoesNotThrow(() -> {
            orderService.moveStatusForward(2, user);
        });

        int statusOrder = dataSource.getResult("SELECT statusId FROM Orders WHERE id = 2");
        assertEquals(3, statusOrder);
    }

    @Test
    public void testToReadyByAdmin() {

        User user = new User();
        user.setUserRole(UserRole.ADMIN);

        assertDoesNotThrow(() -> {
            orderService.moveStatusForward(3, user);
        });


        int statusOrder = dataSource.getResult("SELECT statusId FROM Orders WHERE id = 3");
        assertEquals(4, statusOrder);
    }

    @Test
    public void testToReadyByUser() {

        User user = new User();
        user.setUserRole(UserRole.USER);

        assertThrows(ChangeOrderStatusInvalidException.class, () -> {
            orderService.moveStatusForward(3, user);
        });

        int statusOrder = dataSource.getResult("SELECT statusId FROM Orders WHERE id = 3");
        assertEquals(3, statusOrder);
    }

    @Test
    public void testNextStatusFromReadyByAdmin() {

        User user = new User();
        user.setUserRole(UserRole.ADMIN);

        assertThrows(ChangeOrderStatusInvalidException.class, () -> {
            orderService.moveStatusForward(4, user);
        });

        int statusOrder = dataSource.getResult("SELECT statusId FROM Orders WHERE id = 4");
        assertEquals(4, statusOrder);
    }

    @Test
    public void testNextStatusFromReadyByUser() {

        User user = new User();
        user.setUserRole(UserRole.USER);

        assertThrows(ChangeOrderStatusInvalidException.class, () -> {
            orderService.moveStatusForward(4, user);
        });

        int statusOrder = dataSource.getResult("SELECT statusId FROM Orders WHERE id = 4");
        assertEquals(4, statusOrder);
    }

    @Test
    public void testPreviousStatusFromReadyByAdmin() {
        User user = new User();
        user.setUserRole(UserRole.ADMIN);

        assertThrows(ChangeOrderStatusInvalidException.class, () -> {
            orderService.moveStatusBack(4, user);
        });

        int statusOrder = dataSource.getResult("SELECT statusId FROM Orders WHERE id = 4");
        assertEquals(4, statusOrder);
    }

    @Test
    public void testPreviousStatusFromReadyByUser() {
        User user = new User();
        user.setUserRole(UserRole.USER);

        assertDoesNotThrow(() -> {
            orderService.moveStatusBack(4, user);
        });

        int statusOrder = dataSource.getResult("SELECT statusId FROM Orders WHERE id = 4");
        assertEquals(3, statusOrder);
    }

    @Test
    public void testPreviousStatusFromSelectedByAdmin() {
        User user = new User();
        user.setUserRole(UserRole.ADMIN);

        assertThrows(ChangeOrderStatusInvalidException.class, () -> {
            orderService.moveStatusBack(3, user);
        });

        int statusOrder = dataSource.getResult("SELECT statusId FROM Orders WHERE id = 3");
        assertEquals(3, statusOrder);
    }

    @Test
    public void testPreviousStatusFromSelectedByUser() {

        User user = new User();
        user.setUserRole(UserRole.USER);

        assertThrows(ChangeOrderStatusInvalidException.class, () -> {
            orderService.moveStatusBack(3, user);
        });

        int statusOrder = dataSource.getResult("SELECT statusId FROM Orders WHERE id = 3");
        assertEquals(3, statusOrder);
    }

    @Test
    public void testPreviousStatusFromViewAndSelectByAdmin() {

        User user = new User();
        user.setUserRole(UserRole.ADMIN);

        assertThrows(ChangeOrderStatusInvalidException.class, () -> {
            orderService.moveStatusBack(2, user);
        });

        int statusOrder = dataSource.getResult("SELECT statusId FROM Orders WHERE id = 2");
        assertEquals(2, statusOrder);
    }

    @Test
    public void testPreviousStatusFromViewAndSelectByUser() {

        User user = new User();
        user.setUserRole(UserRole.USER);

        assertThrows(ChangeOrderStatusInvalidException.class, () -> {
            orderService.moveStatusBack(2, user);
        });

        int statusOrder = dataSource.getResult("SELECT statusId FROM Orders WHERE id = 2");
        assertEquals(2, statusOrder);
    }

    @AfterEach
    public void after() throws SQLException {
        dataSource.close();
    }
}
