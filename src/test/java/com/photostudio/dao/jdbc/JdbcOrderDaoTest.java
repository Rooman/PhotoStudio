package com.photostudio.dao.jdbc;

import com.photostudio.dao.jdbc.testUtils.TestDataSource;
import com.photostudio.entity.order.FilterParameters;
import com.photostudio.entity.order.OrderStatus;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JdbcOrderDaoTest {
    private DataSource dataSource = new TestDataSource().init();

    @Test
    public void testGetPartWhereWithAllParams() {
        //prepare
        String expected = " WHERE u.email=? AND o.orderDate>=? AND o.orderDate<=? AND os.statusName=? AND u.phoneNumber=?";
        FilterParameters filterParameters = FilterParameters.builder().email("mail@mail.ua").fromDate(LocalDateTime.MIN)
                .toDate(LocalDateTime.MAX).orderStatus(OrderStatus.NEW).phoneNumber("1234567890").build();

        //when
        JdbcOrderDao jdbcOrderDao = new JdbcOrderDao(dataSource);
        String actual = jdbcOrderDao.getPartWhere(filterParameters);

        //then
        assertEquals(expected, actual);
    }

    @Test
    public void testGetPartWhereWithFewParams() {
        //prepare
        String expected = " WHERE u.email=? AND o.orderDate>=? AND os.statusName=?";
        FilterParameters filterParameters = FilterParameters.builder().email("mail@mail.ua").fromDate(LocalDateTime.MIN)
                .orderStatus(OrderStatus.NEW).build();

        //when
        JdbcOrderDao jdbcOrderDao = new JdbcOrderDao(dataSource);
        String actual = jdbcOrderDao.getPartWhere(filterParameters);

        //then
        assertEquals(expected, actual);
    }

    @Test
    public void testGetPartWhereWithoutParams() {
        //prepare
        String expected = " WHERE ";
        FilterParameters filterParameters = FilterParameters.builder().build();

        //when
        JdbcOrderDao jdbcOrderDao = new JdbcOrderDao(dataSource);
        String actual = jdbcOrderDao.getPartWhere(filterParameters);

        //then
        assertEquals(expected, actual);
    }
}
