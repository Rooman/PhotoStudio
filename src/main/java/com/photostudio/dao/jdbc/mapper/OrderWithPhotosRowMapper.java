package com.photostudio.dao.jdbc.mapper;

import com.photostudio.entity.order.Order;
import com.photostudio.entity.order.OrderStatus;
import com.photostudio.entity.user.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class OrderWithPhotosRowMapper {

    public Order mapRow(ResultSet resultSet, List<String> photoSources) throws SQLException {
        String email = resultSet.getString("email");
        User user = new User();
        user.setEmail(email);

        Order order = Order.builder()
                .id(resultSet.getInt("id"))
                .status(OrderStatus.getOrderStatus(resultSet.getString("statusName")))
                .orderDate(resultSet.getTimestamp("orderDate").toLocalDateTime())
                .user(user)
                .comment(resultSet.getString("comment"))
                .photoSources(photoSources)
                .build();

        return order;
    }
}
