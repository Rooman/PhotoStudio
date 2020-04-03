package com.photostudio.dao.jdbc.mapper;

import com.photostudio.entity.order.OrderStatus;
import com.photostudio.entity.order.Order;
import com.photostudio.entity.user.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class OrderRowMapper {

    public Order mapRow(ResultSet resultSet) throws SQLException {
        String phoneNumber = resultSet.getString("phoneNumber");
        String email = resultSet.getString("email");
        User user = new User();
        user.setEmail(email);
        user.setPhoneNumber(phoneNumber);

        Order order = Order.builder()
                .id(resultSet.getInt("id"))
                .status(OrderStatus.getOrderStatus(resultSet.getString("statusName")))
                .orderDate(resultSet.getTimestamp("orderDate").toLocalDateTime())
                .user(user)
                .photoSources(new ArrayList<>())
                .commentAdmin(resultSet.getString("commentAdmin"))
                .commentUser(resultSet.getString("commentUser"))
                .build();

        return order;
    }
}
