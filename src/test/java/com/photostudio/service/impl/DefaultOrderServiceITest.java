package com.photostudio.service.impl;


import com.photostudio.dao.jdbc.JdbcOrderDao;
import com.photostudio.dao.jdbc.testUtils.TestDataSource;
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

        assertDoesNotThrow(() -> {
            orderService.moveStatusForward(1, UserRole.ADMIN);
        });
        //after
        int statusOrder = dataSource.getResult("SELECT statusId FROM Orders WHERE id = 1");
        assertEquals(2, statusOrder);
    }

    @Test
    public void testToViewAndSelectByUser() {
        //orderService.moveStatusForward(1, UserRole.USER);
        //after
        assertThrows(ChangeOrderStatusInvalidException.class, () -> {
            orderService.moveStatusForward(1, UserRole.USER);
        });

        int statusOrder = dataSource.getResult("SELECT statusId FROM Orders WHERE id = 1");
        assertEquals(1, statusOrder);
    }

    @Test
    public void testToSelectedByAdmin() {

        assertThrows(ChangeOrderStatusInvalidException.class, () -> {
            orderService.moveStatusForward(2, UserRole.ADMIN);
        });

        int statusOrder = dataSource.getResult("SELECT statusId FROM Orders WHERE id = 2");
        assertEquals(2, statusOrder);
    }

    @Test
    public void testToSelectedByUser() {

        assertDoesNotThrow(() -> {
            orderService.moveStatusForward(2, UserRole.USER);
        });

        int statusOrder = dataSource.getResult("SELECT statusId FROM Orders WHERE id = 2");
        assertEquals(3, statusOrder);
    }

    @Test
    public void testToReadyByAdmin() {
        assertDoesNotThrow(() -> {
            orderService.moveStatusForward(3, UserRole.ADMIN);
        });


        int statusOrder = dataSource.getResult("SELECT statusId FROM Orders WHERE id = 3");
        assertEquals(4, statusOrder);
    }

    @Test
    public void testToReadyByUser() {

        assertThrows(ChangeOrderStatusInvalidException.class, () -> {
            orderService.moveStatusForward(3, UserRole.USER);
        });

        int statusOrder = dataSource.getResult("SELECT statusId FROM Orders WHERE id = 3");
        assertEquals(3, statusOrder);
    }

    @Test
    public void testNextStatusFromReadyByAdmin() {
        assertThrows(ChangeOrderStatusInvalidException.class, () -> {
            orderService.moveStatusForward(4, UserRole.ADMIN);
        });

        int statusOrder = dataSource.getResult("SELECT statusId FROM Orders WHERE id = 4");
        assertEquals(4, statusOrder);
    }

    @Test
    public void testNextStatusFromReadyByUser() {

        assertThrows(ChangeOrderStatusInvalidException.class, () -> {
            orderService.moveStatusForward(4, UserRole.USER);
        });

        int statusOrder = dataSource.getResult("SELECT statusId FROM Orders WHERE id = 4");
        assertEquals(4, statusOrder);
    }

    @Test
    public void testPreviousStatusFromReadyByAdmin() {
        assertThrows(ChangeOrderStatusInvalidException.class, () -> {
            orderService.moveStatusBack(4, UserRole.ADMIN);
        });

        int statusOrder = dataSource.getResult("SELECT statusId FROM Orders WHERE id = 4");
        assertEquals(4, statusOrder);
    }

    @Test
    public void testPreviousStatusFromReadyByUser() {

        assertDoesNotThrow(() -> {
            orderService.moveStatusBack(4, UserRole.USER);
        });

        int statusOrder = dataSource.getResult("SELECT statusId FROM Orders WHERE id = 4");
        assertEquals(3, statusOrder);
    }

    @Test
    public void testPreviousStatusFromSelectedByAdmin() {
        assertThrows(ChangeOrderStatusInvalidException.class, () -> {
            orderService.moveStatusBack(3, UserRole.ADMIN);
        });

        int statusOrder = dataSource.getResult("SELECT statusId FROM Orders WHERE id = 3");
        assertEquals(3, statusOrder);
    }

    @Test
    public void testPreviousStatusFromSelectedByUser() {

        assertThrows(ChangeOrderStatusInvalidException.class, () -> {
            orderService.moveStatusBack(3, UserRole.USER);
        });

        int statusOrder = dataSource.getResult("SELECT statusId FROM Orders WHERE id = 3");
        assertEquals(3, statusOrder);
    }

    @Test
    public void testPreviousStatusFromViewAndSelectByAdmin() {
        assertThrows(ChangeOrderStatusInvalidException.class, () -> {
            orderService.moveStatusBack(2, UserRole.ADMIN);
        });

        int statusOrder = dataSource.getResult("SELECT statusId FROM Orders WHERE id = 2");
        assertEquals(2, statusOrder);
    }

    @Test
    public void testPreviousStatusFromViewAndSelectByUser() {

        assertThrows(ChangeOrderStatusInvalidException.class, () -> {
            orderService.moveStatusBack(2, UserRole.USER);
        });

        int statusOrder = dataSource.getResult("SELECT statusId FROM Orders WHERE id = 2");
        assertEquals(2, statusOrder);
    }

    @AfterEach
    public void after() throws SQLException {
        dataSource.close();
    }
}
