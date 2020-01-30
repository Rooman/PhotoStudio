package com.photostudio.dao.jdbc;

import com.photostudio.entity.Order;
import com.photostudio.util.PropertyReader;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.util.List;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class JdbcOrderDaoITest {

    @Test
    public void testGetAll() {
        Properties properties = new PropertyReader("application.properties").getProperties();
        DataSource dataSource = new DataSourceFactory(properties).createDataSource();
        JdbcOrderDao jdbcOrderDao = new JdbcOrderDao(dataSource);
        List<Order> orders = jdbcOrderDao.getAll();

        for (Order order : orders) {
            assertNotNull(order.getOrderDate());
            assertNotNull(order.getComment());
            assertNotNull(order.getEmail());
            assertNotNull(order.getStatus());
        }
    }
}
