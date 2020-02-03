package com.photostudio.dao.jdbc.mapper;


import com.photostudio.entity.order.OrderStatus;
import com.photostudio.entity.order.Order;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderRowMapper {
    public Order mapRow(ResultSet resultSet) throws SQLException {

        Order order = Order.builder()
                .id(resultSet.getInt("id"))
                .status(OrderStatus.getOrderStatus(resultSet.getString("statusName")))
                .orderDate(resultSet.getTimestamp("orderDate").toLocalDateTime())
                .email(resultSet.getString("email"))
                .phoneNumber(resultSet.getString("phoneNumber"))
                .comment(resultSet.getString("comment"))
                .build();

        return order;
    }
}
