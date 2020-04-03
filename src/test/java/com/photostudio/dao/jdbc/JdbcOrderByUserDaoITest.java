package com.photostudio.dao.jdbc;

import com.photostudio.dao.jdbc.testUtils.TestDataSource;
import com.photostudio.entity.order.Order;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JdbcOrderByUserDaoITest {
    private static TestDataSource dataSource = new TestDataSource();
    private static JdbcDataSource jdbcDataSource;

    @BeforeAll
    public static void addTestData() throws SQLException, IOException {
        jdbcDataSource = dataSource.init();
        dataSource.runScript("db/data_order_by_user.sql");
    }

    @Test
    public void testGetOrdersByNotExistingUser() {
        //when
        JdbcOrderDao jdbcOrderDao = new JdbcOrderDao(jdbcDataSource);
        List<Order> orderList = jdbcOrderDao.getOrdersByUserId(10);

        assertEquals(0, orderList.size());
    }

    @Test
    public void testGetOrdersByUserId() {
        //when
        JdbcOrderDao jdbcOrderDao = new JdbcOrderDao(jdbcDataSource);
        List<Order> orderList = jdbcOrderDao.getOrdersByUserId(1);

        assertEquals(1, orderList.size());
    }

    @AfterAll
    public static void closeConnection() throws SQLException {
        dataSource.close();
    }


}
