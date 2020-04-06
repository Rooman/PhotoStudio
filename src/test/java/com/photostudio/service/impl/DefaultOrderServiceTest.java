package com.photostudio.service.impl;

import com.photostudio.dao.jdbc.JdbcOrderDao;
import com.photostudio.dao.jdbc.testUtils.TestDataSource;
import com.photostudio.entity.order.Order;
import com.photostudio.entity.order.OrderStatus;
import com.photostudio.entity.user.UserRole;
import com.photostudio.exception.ChangeOrderStatusInvalidException;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class DefaultOrderServiceTest {
    private static TestDataSource dataSource = new TestDataSource();
    private static DefaultOrderService orderService;


    @BeforeAll
    public static void beforeAll() throws SQLException, IOException {
        JdbcDataSource jdbcDataSource = dataSource.init();
        dataSource.runScript("db/data_change_status.sql");
        dataSource.runScript("db/data_get_orders.sql");
        JdbcOrderDao jdbcOrderDao = new JdbcOrderDao(jdbcDataSource);
        orderService = new DefaultOrderService(jdbcOrderDao);
    }

    @Test
    public void testCheckUserRoleViewAndSelectUser() {
        assertThrows(ChangeOrderStatusInvalidException.class, () -> {
            orderService.checkUserRole(UserRole.USER, OrderStatus.VIEW_AND_SELECT);
        });
    }

    @Test
    public void testCheckUserRoleViewAndSelectAdmin() {
        assertDoesNotThrow(() -> {
            orderService.checkUserRole(UserRole.ADMIN, OrderStatus.VIEW_AND_SELECT);
        });
        assertEquals(true, orderService.checkUserRole(UserRole.ADMIN, OrderStatus.VIEW_AND_SELECT));
    }

    @Test
    public void testCheckUserRoleSelectedUser() {
        assertDoesNotThrow(() -> {
            orderService.checkUserRole(UserRole.USER, OrderStatus.SELECTED);
        });
        assertEquals(true, orderService.checkUserRole(UserRole.USER, OrderStatus.SELECTED));
    }

    @Test
    public void testCheckUserRoleSelectedAdmin() {
        assertThrows(ChangeOrderStatusInvalidException.class, () -> {
            orderService.checkUserRole(UserRole.ADMIN, OrderStatus.SELECTED);
        });
    }

    @Test
    public void testCheckUserRoleReadyUser() {
        assertThrows(ChangeOrderStatusInvalidException.class, () -> {
            orderService.checkUserRole(UserRole.USER, OrderStatus.READY);
        });
    }

    @Test
    public void testCheckUserRoleReadyAdmin() {
        assertDoesNotThrow(() -> {
            orderService.checkUserRole(UserRole.ADMIN, OrderStatus.READY);
        });
        assertEquals(true, orderService.checkUserRole(UserRole.ADMIN, OrderStatus.READY));
    }

    @Test
    void testCheckPhotoToViewAndSelect() {
        assertDoesNotThrow(() -> {
            orderService.checkPhoto(1, OrderStatus.VIEW_AND_SELECT);
        });
        assertEquals(true, orderService.checkPhoto(1, OrderStatus.VIEW_AND_SELECT));
    }

    @Test
    void testCheckPhotoToViewAndSelectWithoutPhoto() {
        assertThrows(ChangeOrderStatusInvalidException.class, () -> {
            orderService.checkPhoto(5, OrderStatus.VIEW_AND_SELECT);
        });
    }

    @Test
    void testCheckPhotoToSelected() {
        assertDoesNotThrow(() -> {
            orderService.checkPhoto(2, OrderStatus.SELECTED);
        });
        assertEquals(true, orderService.checkPhoto(2, OrderStatus.SELECTED));
    }

    @Test
    void testCheckPhotoToSelectedWithoutSelectedPhoto() {
        assertThrows(ChangeOrderStatusInvalidException.class, () -> {
            orderService.checkPhoto(6, OrderStatus.SELECTED);
        });
    }

    @Test
    void testCheckPhotoToReady() {
        assertDoesNotThrow(() -> {
            orderService.checkPhoto(3, OrderStatus.READY);
        });
        assertEquals(true, orderService.checkPhoto(3, OrderStatus.READY));
    }

    @Test
    void testCheckPhotoToReadyWithoutPaidPhoto() {
        assertThrows(ChangeOrderStatusInvalidException.class, () -> {
            orderService.checkPhoto(7, OrderStatus.READY);
        });
    }

    @Test
    void getOrdersWithOrderStatusNotNewByUserIdNotFound() {
        List<Order> orderList = orderService.getOrdersWithOrderStatusNotNewByUserId(4);

        assertEquals(0, orderList.size());
    }

    @Test
    void getOrdersWithOrderStatusNotNewByUserId() {
        List<Order> orderList = orderService.getOrdersWithOrderStatusNotNewByUserId(5);
        assertEquals(2, orderList.size());
    }

    @Test
    void addAllFine() {
    }

    @AfterAll
    public static void after() throws SQLException {
        dataSource.close();
    }
}