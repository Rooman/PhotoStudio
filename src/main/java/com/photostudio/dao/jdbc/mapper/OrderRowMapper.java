package com.photostudio.dao.jdbc.mapper;

import com.photostudio.entity.Order;
import com.photostudio.entity.OrderStatus;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderRowMapper {
    public Order mapRow(ResultSet resultSet) throws SQLException {

        String phoneNumber = resultSet.getString("phoneNumber");
        Order order = Order.builder()
                .id(resultSet.getInt("id"))
                .status(OrderStatus.getOrderStatus(resultSet.getString("statusName")))
                .orderDate(resultSet.getTimestamp("orderDate").toLocalDateTime())
                .email(resultSet.getString("email"))
                .phoneNumber(phoneNumber == null ? null : Long.parseLong(phoneNumber))
                .comment(resultSet.getString("comment"))
                .build();

        return order;
    }
}
