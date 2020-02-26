package com.photostudio.dao.jdbc;

import com.photostudio.dao.jdbc.testUtils.TestDataSource;
import com.photostudio.entity.order.Order;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

 public class JdbcOrderByUserDaoTest {
    private TestDataSource dataSource = new TestDataSource();
    private JdbcDataSource jdbcDataSource;

    @BeforeEach
    public void before() throws IOException, SQLException {
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
    public void testGetOrdersByUserOnlyNew() {
        //when
        JdbcOrderDao jdbcOrderDao = new JdbcOrderDao(jdbcDataSource);
        List<Order> orderList = jdbcOrderDao.getOrdersByUserId(2);

        assertEquals(0, orderList.size());
    }

    @Test
    public void testGetOrdersByUser() {
        //when
        JdbcOrderDao jdbcOrderDao = new JdbcOrderDao(jdbcDataSource);
        List<Order> orderList = jdbcOrderDao.getOrdersByUserId(3);

        assertEquals(3, orderList.size());
    }

    @AfterEach
    public void after() throws SQLException {
        dataSource.close();
    }


}
