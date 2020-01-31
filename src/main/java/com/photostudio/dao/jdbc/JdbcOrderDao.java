package com.photostudio.dao.jdbc;

import com.photostudio.dao.OrderDao;
import com.photostudio.dao.jdbc.mapper.OrderRowMapper;
import com.photostudio.entity.order.Order;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcOrderDao implements OrderDao {

    private static final String GET_ALL_ORDERS = "SELECT o.id id," +
            "os.statusName statusName, " +
            "o.orderDate orderDate, " +
            "u.email email, " +
            "u.phoneNumber phoneNumber, " +
            "o.comment comment " +
            "FROM Orders o " +
            "JOIN OrderStatus os ON o.statusId = os.id " +
            "JOIN Users u ON o.userId = u.id";

    private static final OrderRowMapper ORDER_ROW_MAPPER = new OrderRowMapper();
    private DataSource dataSource;

    public JdbcOrderDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<Order> getAll() {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL_ORDERS);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            List<Order> orders = new ArrayList<>();
            while (resultSet.next()) {
                Order order = ORDER_ROW_MAPPER.mapRow(resultSet);
                orders.add(order);
            }
            return orders;
        } catch (SQLException e) {
            throw new RuntimeException("Error during get all orders", e);
        }
    }
}
