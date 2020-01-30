package com.photostudio.dao.jdbc.mapper;

import com.photostudio.entity.Order;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderRowMapper {
    public Order mapRow(ResultSet resultSet) throws SQLException {

        Order order = Order.builder()
                .id(resultSet.getInt("id"))
                .status(resultSet.getString("statusName"))
                .orderDate(resultSet.getTimestamp("orderDate").toLocalDateTime())
                .email(resultSet.getString("email"))
                .phoneNumber(resultSet.getLong("phoneNumber"))
                .comment(resultSet.getString("comment"))
                .build();

        return order;
    }
}
