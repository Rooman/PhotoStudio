package com.photostudio.dao.jdbc;

import com.photostudio.dao.OrderDao;
import com.photostudio.dao.jdbc.mapper.OrderRowMapper;
import com.photostudio.entity.order.FilterParameters;
import com.photostudio.entity.order.Order;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

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

    //ПОРЯДОК ПАРАМЕТРОВ НЕ МЕНЯТЬ!!!!
    @Override
    public List<Order> getOrdersByParameters(FilterParameters filterParameters) {

        String resultWhere = getPartWhere(filterParameters);
        ;

        if (resultWhere.contains("?")) {

            String selectOrdersByParameters = GET_ALL_ORDERS + resultWhere;

            try (Connection connection = dataSource.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(selectOrdersByParameters)) {
                int count = 1;
                if (filterParameters.getEmail() != null) {
                    preparedStatement.setString(count++, filterParameters.getEmail());
                }
                if (filterParameters.getFromDate() != null) {
                    preparedStatement.setTimestamp(count++, Timestamp.valueOf(filterParameters.getFromDate()));
                }
                if (filterParameters.getToDate() != null) {
                    preparedStatement.setTimestamp(count++, Timestamp.valueOf(filterParameters.getToDate()));
                }
                if (filterParameters.getOrderStatus() != null) {
                    preparedStatement.setString(count++, filterParameters.getOrderStatus().getOrderStatusName());
                }
                if (filterParameters.getPhoneNumber() != null) {
                    preparedStatement.setString(count, filterParameters.getPhoneNumber());
                }
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    List<Order> orders = new ArrayList<>();
                    while (resultSet.next()) {
                        Order order = ORDER_ROW_MAPPER.mapRow(resultSet);
                        orders.add(order);
                    }
                    return orders;
                }
            } catch (SQLException e) {
                throw new RuntimeException("Error during get orders by params", e);
            }
        }
        return getAll();
    }

    protected String getPartWhere(FilterParameters filterParameters) {
        StringJoiner stringJoiner = new StringJoiner(" AND ", " WHERE ", "");
        if (filterParameters.getEmail() != null) {
            stringJoiner.add("u.email=?");
        }
        if (filterParameters.getFromDate() != null) {
            stringJoiner.add("o.orderDate>=?");
        }
        if (filterParameters.getToDate() != null) {
            stringJoiner.add("o.orderDate<=?");
        }
        if (filterParameters.getOrderStatus() != null) {
            stringJoiner.add("os.statusName=?");
        }
        if (filterParameters.getPhoneNumber() != null) {
            stringJoiner.add("u.phoneNumber=?");
        }
        return stringJoiner.toString();
    }
}
