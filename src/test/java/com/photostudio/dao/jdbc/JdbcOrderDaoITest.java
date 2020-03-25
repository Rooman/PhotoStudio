package com.photostudio.dao.jdbc;

import com.photostudio.dao.jdbc.testUtils.TestDataSource;
import com.photostudio.entity.order.FilterParameters;
import com.photostudio.entity.order.OrderStatus;
import com.photostudio.entity.user.User;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.*;
import com.photostudio.entity.order.Order;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JdbcOrderDaoITest {
    private static TestDataSource dataSource = new TestDataSource();
    private static JdbcDataSource jdbcDataSource;

    @BeforeAll
    public static void addTestData() throws SQLException, IOException {
        jdbcDataSource = dataSource.init();
        dataSource.runScript("db/clear_orders.sql");
        dataSource.runScript("db/clear_users.sql");
        dataSource.runScript("db/data.sql");
    }

    @Test
    public void testGetAll() {
        //prepare
        List<Order> expected = new ArrayList<>();
        User user1 = new User();
        user1.setEmail("mymail@d.com");
        user1.setPhoneNumber("380731234567");

        User user2 = new User();
        user2.setEmail("mymail2@d.com");

        Order order1 = Order.builder().id(1)
                .orderDate(LocalDateTime.of(2020, 1, 15, 18, 38, 33))
                .comment("NEW").user(user1).status(OrderStatus.NEW).build();
        expected.add(order1);
        Order order2 = Order.builder().id(2)
                .orderDate(LocalDateTime.of(2020, 1, 21, 18, 38, 33))
                .comment("OLD").user(user2).status(OrderStatus.VIEW_AND_SELECT).build();
        expected.add(order2);
        Order order3 = Order.builder().id(3)
                .orderDate(LocalDateTime.of(2020, 1, 29, 18, 38, 33))
                .user(user1).status(OrderStatus.SELECTED).build();
        expected.add(order3);
        Order order4 = Order.builder().id(4)
                .orderDate(LocalDateTime.of(2020, 1, 29, 18, 38, 33))
                .user(user2).status(OrderStatus.READY).build();
        expected.add(order4);


        //when
        JdbcOrderDao jdbcOrderDao = new JdbcOrderDao(jdbcDataSource);
        List<Order> actual = jdbcOrderDao.getAll();

        //then
        assertEquals(expected.size(), actual.size());
        for (Order expectedOrder : expected) {
            actual.removeIf(x -> x.equals(expectedOrder));
        }
        assertEquals(0, actual.size());
    }

    @Test
    public void testGetOrderByIdInStatusNew() {
        //prepare
        User user = new User();
        user.setEmail("mymail@d.com");

        List<String> photoList = new ArrayList<>();
        photoList.add("/home/myPhoto1");
        photoList.add("/home/myPhoto2");

        Order expected = Order.builder().id(1)
                .orderDate(LocalDateTime.of(2020, 1, 15, 18, 38, 33))
                .comment("NEW").user(user).status(OrderStatus.NEW).photoSources(photoList).build();

        //when
        JdbcOrderDao jdbcOrderDao = new JdbcOrderDao(jdbcDataSource);
        Order actual = jdbcOrderDao.getOrderByIdInStatusNew(1);

        //then
        LocalDateTime expectedDateTime = LocalDateTime.of(2020, 1, 15, 18, 38, 33);

        assertEquals(1, expected.getId());
        assertEquals("NEW", expected.getStatus().getOrderStatusName());
        assertEquals("mymail@d.com", expected.getUser().getEmail());
        assertEquals("NEW", expected.getComment());
        assertEquals(expectedDateTime, expected.getOrderDate());

        for (String expectedSource : expected.getPhotoSources()) {
            photoList.removeIf(x -> x.equals(expectedSource));
        }
    }

    @Test
    public void testGetOrdersByParamsWithoutParams() {
        //prepare
        FilterParameters filterParameters = FilterParameters.builder().build();
        List<Order> expected = new ArrayList<>();

        User user1 = new User();
        user1.setEmail("mymail@d.com");
        user1.setPhoneNumber("380731234567");

        User user2 = new User();
        user2.setEmail("mymail2@d.com");

        Order order1 = Order.builder().id(1)
                .orderDate(LocalDateTime.of(2020, 1, 15, 18, 38, 33))
                .comment("NEW").user(user1).status(OrderStatus.NEW).build();
        expected.add(order1);
        Order order2 = Order.builder().id(2)
                .orderDate(LocalDateTime.of(2020, 1, 21, 18, 38, 33))
                .comment("OLD").user(user2).status(OrderStatus.VIEW_AND_SELECT).build();
        expected.add(order2);
        Order order3 = Order.builder().id(3)
                .orderDate(LocalDateTime.of(2020, 1, 29, 18, 38, 33))
                .user(user1).status(OrderStatus.SELECTED).build();
        expected.add(order3);
        Order order4 = Order.builder().id(4)
                .orderDate(LocalDateTime.of(2020, 1, 29, 18, 38, 33))
                .user(user2).status(OrderStatus.READY).build();
        expected.add(order4);


        //when
        JdbcOrderDao jdbcOrderDao = new JdbcOrderDao(jdbcDataSource);
        List<Order> actual = jdbcOrderDao.getOrdersByParameters(filterParameters);

        //then
        assertEquals(expected.size(), actual.size());
        for (Order expectedOrder : expected) {
            actual.removeIf(x -> x.equals(expectedOrder));
        }
        assertEquals(0, actual.size());
    }

    @Test
    public void testGetOrdersByParamsWithDateParams() {
        //prepare
        FilterParameters filterParameters = FilterParameters.builder()
                .fromDate(LocalDateTime.of(2020, 1, 14, 0, 0, 0))
                .toDate(LocalDateTime.of(2020, 1, 22, 0, 0, 0))
                .build();
        List<Order> expected = new ArrayList<>();

        User user1 = new User();
        user1.setEmail("mymail@d.com");
        user1.setPhoneNumber("380731234567");

        User user2 = new User();
        user2.setEmail("mymail2@d.com");

        Order order1 = Order.builder().id(1)
                .orderDate(LocalDateTime.of(2020, 1, 15, 18, 38, 33))
                .comment("NEW").user(user1).status(OrderStatus.NEW).build();
        expected.add(order1);
        Order order2 = Order.builder().id(2)
                .orderDate(LocalDateTime.of(2020, 1, 21, 18, 38, 33))
                .comment("OLD").user(user2).status(OrderStatus.VIEW_AND_SELECT).build();
        expected.add(order2);

        //when
        JdbcOrderDao jdbcOrderDao = new JdbcOrderDao(jdbcDataSource);
        List<Order> actual = jdbcOrderDao.getOrdersByParameters(filterParameters);

        //then
        assertEquals(expected.size(), actual.size());
        for (Order expectedOrder : expected) {
            actual.removeIf(x -> x.equals(expectedOrder));
        }
        assertEquals(0, actual.size());
    }

    @Test
    public void testGetOrdersByParamsWithEmailParams() {
        //prepare
        FilterParameters filterParameters = FilterParameters.builder()
                .email("mymail@d.com")
                .build();
        List<Order> expected = new ArrayList<>();

        User user1 = new User();
        user1.setEmail("mymail@d.com");
        user1.setPhoneNumber("380731234567");

        Order order1 = Order.builder().id(1)
                .orderDate(LocalDateTime.of(2020, 1, 15, 18, 38, 33))
                .comment("NEW").user(user1).status(OrderStatus.NEW).build();
        expected.add(order1);
        Order order3 = Order.builder().id(3)
                .orderDate(LocalDateTime.of(2020, 1, 29, 18, 38, 33))
                .user(user1).status(OrderStatus.SELECTED).build();
        expected.add(order3);

        //when
        JdbcOrderDao jdbcOrderDao = new JdbcOrderDao(jdbcDataSource);
        List<Order> actual = jdbcOrderDao.getOrdersByParameters(filterParameters);

        //then
        assertEquals(expected.size(), actual.size());
        for (Order expectedOrder : expected) {
            actual.removeIf(x -> x.equals(expectedOrder));
        }
        assertEquals(0, actual.size());
    }

    @Test
    public void testGetOrdersByParamsWithPhoneParams() {
        //prepare
        FilterParameters filterParameters = FilterParameters.builder()
                .phoneNumber("380731234567")
                .build();
        List<Order> expected = new ArrayList<>();

        User user1 = new User();
        user1.setEmail("mymail@d.com");
        user1.setPhoneNumber("380731234567");

        Order order1 = Order.builder().id(1)
                .orderDate(LocalDateTime.of(2020, 1, 15, 18, 38, 33))
                .comment("NEW").user(user1).status(OrderStatus.NEW).build();
        expected.add(order1);
        Order order3 = Order.builder().id(3)
                .orderDate(LocalDateTime.of(2020, 1, 29, 18, 38, 33))
                .user(user1).status(OrderStatus.SELECTED).build();
        expected.add(order3);

        //when
        JdbcOrderDao jdbcOrderDao = new JdbcOrderDao(jdbcDataSource);
        List<Order> actual = jdbcOrderDao.getOrdersByParameters(filterParameters);

        //then
        assertEquals(expected.size(), actual.size());
        for (Order expectedOrder : expected) {
            actual.removeIf(x -> x.equals(expectedOrder));
        }
        assertEquals(0, actual.size());
    }

    @Test
    public void testGetOrdersByParamsWithOrderStatusParams() {
        //prepare
        FilterParameters filterParameters = FilterParameters.builder()
                .orderStatus(OrderStatus.READY)
                .build();
        List<Order> expected = new ArrayList<>();

        User user2 = new User();
        user2.setEmail("mymail2@d.com");

        Order order4 = Order.builder().id(4)
                .orderDate(LocalDateTime.of(2020, 1, 29, 18, 38, 33))
                .user(user2).status(OrderStatus.READY).build();
        expected.add(order4);

        //when
        JdbcOrderDao jdbcOrderDao = new JdbcOrderDao(jdbcDataSource);
        List<Order> actual = jdbcOrderDao.getOrdersByParameters(filterParameters);

        //then
        assertEquals(expected.size(), actual.size());
        for (Order expectedOrder : expected) {
            actual.removeIf(x -> x.equals(expectedOrder));
        }
        assertEquals(0, actual.size());
    }

    @Test
    public void testGetOrdersByParamsWithAllParams() {
        //prepare
        FilterParameters filterParameters = FilterParameters.builder()
                .orderStatus(OrderStatus.NEW)
                .fromDate(LocalDateTime.of(2018, 1, 15, 18, 38, 33))
                .toDate(LocalDateTime.of(2021, 1, 15, 18, 38, 33))
                .phoneNumber("380731234567")
                .email("mymail@d.com")
                .build();
        List<Order> expected = new ArrayList<>();

        User user1 = new User();
        user1.setEmail("mymail@d.com");
        user1.setPhoneNumber("380731234567");

        Order order1 = Order.builder().id(1)
                .orderDate(LocalDateTime.of(2020, 1, 15, 18, 38, 33))
                .comment("NEW").user(user1).status(OrderStatus.NEW).build();
        expected.add(order1);

        //when
        JdbcOrderDao jdbcOrderDao = new JdbcOrderDao(jdbcDataSource);
        List<Order> actual = jdbcOrderDao.getOrdersByParameters(filterParameters);

        //then
        assertEquals(expected.size(), actual.size());
        for (Order expectedOrder : expected) {
            actual.removeIf(x -> x.equals(expectedOrder));
        }
        assertEquals(0, actual.size());
    }

    @AfterAll
    public static void closeConnection() throws SQLException, IOException {
        dataSource.runScript("db/clear_orders.sql");
        dataSource.runScript("db/clear_users.sql");
        dataSource.close();
    }
}
