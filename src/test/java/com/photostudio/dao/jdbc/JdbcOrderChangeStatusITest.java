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

public class JdbcOrderChangeStatusITest {
    private TestDataSource dataSource = new TestDataSource();
    private JdbcDataSource jdbcDataSource;

    @BeforeEach
    public void before() throws SQLException, IOException {
        jdbcDataSource = dataSource.init();
        dataSource.runScript("db/data.sql");
    }

    @Test
    public void testStatusToViewAndSelect() {
        //when change from  NEW
        JdbcOrderDao jdbcOrderDao = new JdbcOrderDao(jdbcDataSource);
        jdbcOrderDao.changeOrderStatus(1, 2);

        //after
        int statusOrder = dataSource.getResult("SELECT statusId FROM Orders WHERE id = 1");
        assertEquals(2, statusOrder);
    }

    @Test
    public void testStatusToSelected() {
        //when change from ViewAndSelect
        JdbcOrderDao jdbcOrderDao = new JdbcOrderDao(jdbcDataSource);
        jdbcOrderDao.changeOrderStatus(2, 3);

        //after
        int statusOrder = dataSource.getResult("SELECT statusId FROM Orders WHERE id = 2");
        assertEquals(3, statusOrder);
    }

    @Test
    public void testStatusToSelectedRepeat() {
        //when change from ViewAndSelect
        JdbcOrderDao jdbcOrderDao = new JdbcOrderDao(jdbcDataSource);
        jdbcOrderDao.changeOrderStatus(3, 3);

        //after
        int statusOrder = dataSource.getResult("SELECT statusId FROM Orders WHERE id = 3");
        assertEquals(3, statusOrder);
    }

    @Test
    public void testStatusToReady() {
        //when change from Selected
        JdbcOrderDao jdbcOrderDao = new JdbcOrderDao(jdbcDataSource);
        jdbcOrderDao.changeOrderStatus(3, 4);

        //after
        int statusOrder = dataSource.getResult("SELECT statusId FROM Orders WHERE id = 3");
        assertEquals(4, statusOrder);
    }


    @Test
    public void testStatusToSelectedFromReady() {
        //when change from ViewAndSelect
        JdbcOrderDao jdbcOrderDao = new JdbcOrderDao(jdbcDataSource);
        jdbcOrderDao.changeOrderStatus(4, 3);

        //after
        int statusOrder = dataSource.getResult("SELECT statusId FROM Orders WHERE id = 4");
        assertEquals(3, statusOrder);
    }

    @AfterEach
    public void after() throws SQLException {
        dataSource.close();
    }
}