package com.photostudio.dao.jdbc;


import com.photostudio.dao.jdbc.testUtils.TestDataSource;
import com.photostudio.entity.order.FilterParameters;
import com.photostudio.entity.order.Order;
import com.photostudio.entity.order.OrderStatus;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JdbcOrderDaoSortITest {
    private static TestDataSource dataSource = new TestDataSource();
    private static JdbcDataSource jdbcDataSource;

    @BeforeAll
    public static void addTestData() throws SQLException, IOException {
        jdbcDataSource = dataSource.init();
        dataSource.runScript("db/clear_orders.sql");
        dataSource.runScript("db/clear_users.sql");
        dataSource.runScript("db/data_sort_orders.sql");
    }

    @Test
    public void testSortGetAll() {
        Integer[] expectedResult = {5, 1, 6, 2, 3, 7, 8, 4};

        //when
        JdbcOrderDao jdbcOrderDao = new JdbcOrderDao(jdbcDataSource);
        List<Order> actual = jdbcOrderDao.getAll();

        assertEquals(expectedResult.length, actual.size());

        for (int i = 0; i < actual.size(); i++) {
            assertEquals(expectedResult[i], actual.get(i).getId());
        }
    }

    @Test
    public void testSortGetOrdersByUserId() {
        Integer[] expectedResult = {6, 2, 8, 4};

        //when
        JdbcOrderDao jdbcOrderDao = new JdbcOrderDao(jdbcDataSource);
        List<Order> actual = jdbcOrderDao.getOrdersByUserId(2);

        assertEquals(expectedResult.length, actual.size());

        for (int i = 0; i < actual.size(); i++) {
            assertEquals(expectedResult[i], actual.get(i).getId());
        }
    }

    @Test
    public void testSortGetOrdersByParameters() {
        Integer[] expectedResult = {8, 4};

        FilterParameters filterParameters = FilterParameters.builder()
                .orderStatus(OrderStatus.READY)
                .build();
        //when
        JdbcOrderDao jdbcOrderDao = new JdbcOrderDao(jdbcDataSource);
        List<Order> actual = jdbcOrderDao.getOrdersByParameters(filterParameters);

        assertEquals(expectedResult.length, actual.size());

        for (int i = 0; i < actual.size(); i++) {
            assertEquals(expectedResult[i], actual.get(i).getId());
        }
    }

    @AfterAll
    public static void closeConnection() throws SQLException, IOException {
        dataSource.runScript("db/clear_orders.sql");
        dataSource.runScript("db/clear_users.sql");
        dataSource.close();
    }
}
