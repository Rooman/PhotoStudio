package com.photostudio.dao.jdbc;

import com.photostudio.entity.order.FilterParameters;
import com.photostudio.entity.order.OrderStatus;
import org.h2.jdbcx.JdbcDataSource;
import org.h2.tools.RunScript;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import com.photostudio.entity.order.Order;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JdbcOrderDaoITest {
    private Connection connection;
    private JdbcDataSource jdbcDataSource;

    @BeforeEach
    public void before() throws SQLException, FileNotFoundException {
        jdbcDataSource = new JdbcDataSource();
        jdbcDataSource.setURL("jdbc:h2:mem:photostudio;MODE=mysql");
        jdbcDataSource.setUser("h2");
        jdbcDataSource.setPassword("h2");

        connection = jdbcDataSource.getConnection();

        FileReader fileSchema = new FileReader("db/schema.sql");

        RunScript.execute(connection, fileSchema);

        FileReader fileData = new FileReader("db/data.sql");

        RunScript.execute(connection, fileData);
    }

    @Test
    public void testGetAll() {
        //prepare
        List<Order> expected = new ArrayList<>();
        Order order1 = Order.builder().id(1)
                .orderDate(LocalDateTime.of(2020, 1, 15, 18, 38, 33))
                .comment("NEW").email("mymail@d.com").phoneNumber(380731234567L).status(OrderStatus.NEW).build();
        expected.add(order1);
        Order order2 = Order.builder().id(2)
                .orderDate(LocalDateTime.of(2020, 1, 21, 18, 38, 33))
                .comment("OLD").email("mymail2@d.com").status(OrderStatus.VIEW_AND_SELECT).build();
        expected.add(order2);
        Order order3 = Order.builder().id(3)
                .orderDate(LocalDateTime.of(2020, 1, 29, 18, 38, 33))
                .email("mymail@d.com").phoneNumber(380731234567L).status(OrderStatus.SELECTED).build();
        expected.add(order3);
        Order order4 = Order.builder().id(4)
                .orderDate(LocalDateTime.of(2020, 1, 29, 18, 38, 33))
                .email("mymail2@d.com").status(OrderStatus.READY).build();
        expected.add(order4);


        //when
        JdbcOrderDao jdbcOrderDao = new JdbcOrderDao(jdbcDataSource);
        List<Order> actual = jdbcOrderDao.getAll();

        //then
        assertEquals(expected.size(), actual.size());
        for (int index = 0; index < expected.size(); index++) {
            assertEquals(expected.get(index).getId(), actual.get(index).getId());
            assertEquals(expected.get(index).getComment(), actual.get(index).getComment());
            assertEquals(expected.get(index).getEmail(), actual.get(index).getEmail());
            assertEquals(expected.get(index).getOrderDate(), actual.get(index).getOrderDate());
            assertEquals(expected.get(index).getStatus(), actual.get(index).getStatus());
            assertEquals(expected.get(index).getPhoneNumber(), actual.get(index).getPhoneNumber());
        }
    }

    @Test
    public void testGetOrdersByParamsWithoutParams(){
        //prepare
        FilterParameters filterParameters = FilterParameters.builder().build();
        List<Order> expected = new ArrayList<>();
        Order order1 = Order.builder().id(1)
                .orderDate(LocalDateTime.of(2020, 1, 15, 18, 38, 33))
                .comment("NEW").email("mymail@d.com").phoneNumber(380731234567L).status(OrderStatus.NEW).build();
        expected.add(order1);
        Order order2 = Order.builder().id(2)
                .orderDate(LocalDateTime.of(2020, 1, 21, 18, 38, 33))
                .comment("OLD").email("mymail2@d.com").status(OrderStatus.VIEW_AND_SELECT).build();
        expected.add(order2);
        Order order3 = Order.builder().id(3)
                .orderDate(LocalDateTime.of(2020, 1, 29, 18, 38, 33))
                .email("mymail@d.com").phoneNumber(380731234567L).status(OrderStatus.SELECTED).build();
        expected.add(order3);
        Order order4 = Order.builder().id(4)
                .orderDate(LocalDateTime.of(2020, 1, 29, 18, 38, 33))
                .email("mymail2@d.com").status(OrderStatus.READY).build();
        expected.add(order4);


        //when
        JdbcOrderDao jdbcOrderDao = new JdbcOrderDao(jdbcDataSource);
        List<Order> actual = jdbcOrderDao.getOrdersByParameters(filterParameters);

        //then
        assertEquals(expected.size(), actual.size());
        for (int index = 0; index < expected.size(); index++) {
            assertEquals(expected.get(index).getId(), actual.get(index).getId());
            assertEquals(expected.get(index).getComment(), actual.get(index).getComment());
            assertEquals(expected.get(index).getEmail(), actual.get(index).getEmail());
            assertEquals(expected.get(index).getOrderDate(), actual.get(index).getOrderDate());
            assertEquals(expected.get(index).getStatus(), actual.get(index).getStatus());
            assertEquals(expected.get(index).getPhoneNumber(), actual.get(index).getPhoneNumber());
        }
    }
    @Test
    public void testGetOrdersByParamsWithDateParams(){
        //prepare
        FilterParameters filterParameters = FilterParameters.builder()
                .fromDate(LocalDateTime.of(2020, 1, 14,0, 0, 0))
                .toDate(LocalDateTime.of(2020, 1, 22, 0, 0, 0))
                .build();
        List<Order> expected = new ArrayList<>();
        Order order1 = Order.builder().id(1)
                .orderDate(LocalDateTime.of(2020, 1, 15, 18, 38, 33))
                .comment("NEW").email("mymail@d.com").phoneNumber(380731234567L).status(OrderStatus.NEW).build();
        expected.add(order1);
        Order order2 = Order.builder().id(2)
                .orderDate(LocalDateTime.of(2020, 1, 21, 18, 38, 33))
                .comment("OLD").email("mymail2@d.com").status(OrderStatus.VIEW_AND_SELECT).build();
        expected.add(order2);

        //when
        JdbcOrderDao jdbcOrderDao = new JdbcOrderDao(jdbcDataSource);
        List<Order> actual = jdbcOrderDao.getOrdersByParameters(filterParameters);

        //then
        assertEquals(expected.size(), actual.size());
        for (int index = 0; index < expected.size(); index++) {
            assertEquals(expected.get(index).getId(), actual.get(index).getId());
            assertEquals(expected.get(index).getComment(), actual.get(index).getComment());
            assertEquals(expected.get(index).getEmail(), actual.get(index).getEmail());
            assertEquals(expected.get(index).getOrderDate(), actual.get(index).getOrderDate());
            assertEquals(expected.get(index).getStatus(), actual.get(index).getStatus());
            assertEquals(expected.get(index).getPhoneNumber(), actual.get(index).getPhoneNumber());
        }
    }

    @Test
    public void testGetOrdersByParamsWithEmailParams(){
        //prepare
        FilterParameters filterParameters = FilterParameters.builder()
                .email("mymail@d.com")
                .build();
        List<Order> expected = new ArrayList<>();
        Order order1 = Order.builder().id(1)
                .orderDate(LocalDateTime.of(2020, 1, 15, 18, 38, 33))
                .comment("NEW").email("mymail@d.com").phoneNumber(380731234567L).status(OrderStatus.NEW).build();
        expected.add(order1);
        Order order3 = Order.builder().id(3)
                .orderDate(LocalDateTime.of(2020, 1, 29, 18, 38, 33))
                .email("mymail@d.com").phoneNumber(380731234567L).status(OrderStatus.SELECTED).build();
        expected.add(order3);

        //when
        JdbcOrderDao jdbcOrderDao = new JdbcOrderDao(jdbcDataSource);
        List<Order> actual = jdbcOrderDao.getOrdersByParameters(filterParameters);

        //then
        assertEquals(expected.size(), actual.size());
        for (int index = 0; index < expected.size(); index++) {
            assertEquals(expected.get(index).getId(), actual.get(index).getId());
            assertEquals(expected.get(index).getComment(), actual.get(index).getComment());
            assertEquals(expected.get(index).getEmail(), actual.get(index).getEmail());
            assertEquals(expected.get(index).getOrderDate(), actual.get(index).getOrderDate());
            assertEquals(expected.get(index).getStatus(), actual.get(index).getStatus());
            assertEquals(expected.get(index).getPhoneNumber(), actual.get(index).getPhoneNumber());
        }
    }

    @Test
    public void testGetOrdersByParamsWithPhoneParams(){
        //prepare
        FilterParameters filterParameters = FilterParameters.builder()
                .phoneNumber(380731234567L)
                .build();
        List<Order> expected = new ArrayList<>();
        Order order1 = Order.builder().id(1)
                .orderDate(LocalDateTime.of(2020, 1, 15, 18, 38, 33))
                .comment("NEW").email("mymail@d.com").phoneNumber(380731234567L).status(OrderStatus.NEW).build();
        expected.add(order1);
        Order order3 = Order.builder().id(3)
                .orderDate(LocalDateTime.of(2020, 1, 29, 18, 38, 33))
                .email("mymail@d.com").phoneNumber(380731234567L).status(OrderStatus.SELECTED).build();
        expected.add(order3);

        //when
        JdbcOrderDao jdbcOrderDao = new JdbcOrderDao(jdbcDataSource);
        List<Order> actual = jdbcOrderDao.getOrdersByParameters(filterParameters);

        //then
        assertEquals(expected.size(), actual.size());
        for (int index = 0; index < expected.size(); index++) {
            assertEquals(expected.get(index).getId(), actual.get(index).getId());
            assertEquals(expected.get(index).getComment(), actual.get(index).getComment());
            assertEquals(expected.get(index).getEmail(), actual.get(index).getEmail());
            assertEquals(expected.get(index).getOrderDate(), actual.get(index).getOrderDate());
            assertEquals(expected.get(index).getStatus(), actual.get(index).getStatus());
            assertEquals(expected.get(index).getPhoneNumber(), actual.get(index).getPhoneNumber());
        }
    }

    @Test
    public void testGetOrdersByParamsWithOrderStatusParams(){
        //prepare
        FilterParameters filterParameters = FilterParameters.builder()
                .orderStatus(OrderStatus.READY)
                .build();
        List<Order> expected = new ArrayList<>();
        Order order4 = Order.builder().id(4)
                .orderDate(LocalDateTime.of(2020, 1, 29, 18, 38, 33))
                .email("mymail2@d.com").status(OrderStatus.READY).build();
        expected.add(order4);

        //when
        JdbcOrderDao jdbcOrderDao = new JdbcOrderDao(jdbcDataSource);
        List<Order> actual = jdbcOrderDao.getOrdersByParameters(filterParameters);

        //then
        assertEquals(expected.size(), actual.size());
        for (int index = 0; index < expected.size(); index++) {
            assertEquals(expected.get(index).getId(), actual.get(index).getId());
            assertEquals(expected.get(index).getComment(), actual.get(index).getComment());
            assertEquals(expected.get(index).getEmail(), actual.get(index).getEmail());
            assertEquals(expected.get(index).getOrderDate(), actual.get(index).getOrderDate());
            assertEquals(expected.get(index).getStatus(), actual.get(index).getStatus());
            assertEquals(expected.get(index).getPhoneNumber(), actual.get(index).getPhoneNumber());
        }
    }

    @Test
    public void testGetOrdersByParamsWithAllParams(){
        //prepare
        FilterParameters filterParameters = FilterParameters.builder()
                .orderStatus(OrderStatus.NEW)
                .fromDate(LocalDateTime.of(2018, 1, 15, 18, 38, 33))
                .toDate(LocalDateTime.of(2021, 1, 15, 18, 38, 33))
                .phoneNumber(380731234567L)
                .email("mymail@d.com")
                .build();
        List<Order> expected = new ArrayList<>();
        Order order1 = Order.builder().id(1)
                .orderDate(LocalDateTime.of(2020, 1, 15, 18, 38, 33))
                .comment("NEW").email("mymail@d.com").phoneNumber(380731234567L).status(OrderStatus.NEW).build();
        expected.add(order1);

        //when
        JdbcOrderDao jdbcOrderDao = new JdbcOrderDao(jdbcDataSource);
        List<Order> actual = jdbcOrderDao.getOrdersByParameters(filterParameters);

        //then
        assertEquals(expected.size(), actual.size());
        for (int index = 0; index < expected.size(); index++) {
            assertEquals(expected.get(index).getId(), actual.get(index).getId());
            assertEquals(expected.get(index).getComment(), actual.get(index).getComment());
            assertEquals(expected.get(index).getEmail(), actual.get(index).getEmail());
            assertEquals(expected.get(index).getOrderDate(), actual.get(index).getOrderDate());
            assertEquals(expected.get(index).getStatus(), actual.get(index).getStatus());
            assertEquals(expected.get(index).getPhoneNumber(), actual.get(index).getPhoneNumber());
        }
    }


    @AfterEach
    public void after() throws SQLException {
        connection.close();
    }
}
