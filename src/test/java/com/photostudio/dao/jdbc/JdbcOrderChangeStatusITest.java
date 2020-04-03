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
        dataSource.runScript("db/data_change_status.sql");
    }

    @Test
    public void testStatusToViewAndSelect() throws SQLException {
        //when change from  NEW
        JdbcOrderDao jdbcOrderDao = new JdbcOrderDao(jdbcDataSource);
        jdbcOrderDao.changeOrderStatus(1, 2);

        //after
        int statusOrder = dataSource.getResult("SELECT statusId FROM Orders WHERE id = 1");
        assertEquals(2, statusOrder);
    }

    @Test
    public void testStatusToSelected() throws SQLException {
        //when change from ViewAndSelect
        JdbcOrderDao jdbcOrderDao = new JdbcOrderDao(jdbcDataSource);
        jdbcOrderDao.changeOrderStatus(2, 3);

        //after
        int statusOrder = dataSource.getResult("SELECT statusId FROM Orders WHERE id = 2");
        assertEquals(3, statusOrder);
    }

    @Test
    public void testStatusToSelectedRepeat() throws SQLException {
        //when change from ViewAndSelect
        JdbcOrderDao jdbcOrderDao = new JdbcOrderDao(jdbcDataSource);
        jdbcOrderDao.changeOrderStatus(3, 3);

        //after
        int statusOrder = dataSource.getResult("SELECT statusId FROM Orders WHERE id = 3");
        assertEquals(3, statusOrder);
    }

    @Test
    public void testStatusToReady() throws SQLException {
        //when change from Selected
        JdbcOrderDao jdbcOrderDao = new JdbcOrderDao(jdbcDataSource);
        jdbcOrderDao.changeOrderStatus(3, 4);

        //after
        int statusOrder = dataSource.getResult("SELECT statusId FROM Orders WHERE id = 3");
        assertEquals(4, statusOrder);
    }


    @Test
    public void testStatusToSelectedFromReady() throws SQLException {
        //when change from ViewAndSelect
        JdbcOrderDao jdbcOrderDao = new JdbcOrderDao(jdbcDataSource);
        jdbcOrderDao.changeOrderStatus(4, 3);

        //after
        int statusOrder = dataSource.getResult("SELECT statusId FROM Orders WHERE id = 4");
        assertEquals(3, statusOrder);
    }

    @Test
    public void testEditOrderByAdmin() throws SQLException {
        JdbcOrderDao jdbcOrderDao = new JdbcOrderDao(jdbcDataSource);
        jdbcOrderDao.editOrderByAdmin(1, 1, "Comment from Admin");

        int newUserId = dataSource.getResult("SELECT userId FROM Orders WHERE id = 1");
        assertEquals(1, newUserId);

        String commentAdmin = dataSource.getString("SELECT commentAdmin FROM Orders WHERE id = 1");
        assertEquals("Comment from Admin", commentAdmin);
    }

    @Test
    public void testEditOrderByUser() throws SQLException {
        JdbcOrderDao jdbcOrderDao = new JdbcOrderDao(jdbcDataSource);
        jdbcOrderDao.editOrderByUser(1, "Comment from User");

        String commentAdmin = dataSource.getString("SELECT commentUser FROM Orders WHERE id = 1");
        assertEquals("Comment from User", commentAdmin);
    }

    @AfterEach
    public void after() throws SQLException {
        dataSource.close();
    }
}