package com.photostudio.dao.jdbc;

import com.photostudio.dao.jdbc.testUtils.TestDataSource;
import com.photostudio.entity.order.OrderStatus;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class JdbcOrderChangeStatusITest {
    private TestDataSource dataSource = new TestDataSource();
    private JdbcOrderDao jdbcOrderDao;

    @BeforeEach
    public void before() throws SQLException, IOException {
        JdbcDataSource jdbcDataSource = dataSource.init();
        dataSource.runScript("db/data.sql");
        jdbcOrderDao = new JdbcOrderDao(jdbcDataSource);
    }

    @Test
    public void testStatusToViewAndSelect() {
        //when change from  NEW
        jdbcOrderDao.changeOrderStatus(1, true);

        //after
        int statusOrder = dataSource.getResult("SELECT statusId FROM Orders WHERE id = 1");
        assertEquals(2, statusOrder);
    }

    @Test
    public void testStatusToSelected() {
        //when change from ViewAndSelect
        jdbcOrderDao.changeOrderStatus(2, true);

        //after
        int statusOrder = dataSource.getResult("SELECT statusId FROM Orders WHERE id = 2");
        assertEquals(3, statusOrder);
    }


    @Test
    public void testStatusToReady() {
        //when change from Selected
        jdbcOrderDao.changeOrderStatus(3, true);

        //after
        int statusOrder = dataSource.getResult("SELECT statusId FROM Orders WHERE id = 3");
        assertEquals(4, statusOrder);
    }


    @Test
    public void testStatusToSelectedFromReady() {
        //when change from ViewAndSelect
        jdbcOrderDao.changeOrderStatus(4, false);

        //after
        int statusOrder = dataSource.getResult("SELECT statusId FROM Orders WHERE id = 4");
        assertEquals(3, statusOrder);
    }

    @Test
    public void testGetOrderStatus() {
        OrderStatus orderStatus = jdbcOrderDao.getOrderStatus(1);
        assertEquals(OrderStatus.NEW.getOrderStatusName(), orderStatus.getOrderStatusName());
    }

    @Test
    public void testGetOrderStatusNotExist() {
        assertThrows(RuntimeException.class, () -> {
            OrderStatus orderStatus = jdbcOrderDao.getOrderStatus(10);
        });
    }

    @AfterEach
    public void after() throws SQLException {
        dataSource.close();
    }
}
