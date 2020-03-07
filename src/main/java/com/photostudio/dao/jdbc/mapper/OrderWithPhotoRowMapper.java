package com.photostudio.dao.jdbc.mapper;

import com.photostudio.entity.order.Order;
import com.photostudio.entity.order.OrderStatus;
import com.photostudio.entity.user.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderWithPhotoRowMapper {
    public Order mapRow(ResultSet resultSet) throws SQLException {
        boolean isFirst = false;
        List<String> photoSources = new ArrayList<>();

        Order.OrderBuilder orderBuilder = Order.builder();

        while (resultSet.next()) {
            if (!isFirst) {
                String email = resultSet.getString("email");
                User user = new User();
                user.setEmail(email);

                orderBuilder.id(resultSet.getInt("id"))
                        .status(OrderStatus.getOrderStatus(resultSet.getString("statusName")))
                        .orderDate(resultSet.getTimestamp("orderDate").toLocalDateTime())
                        .user(user)
                        .comment(resultSet.getString("comment"));

                isFirst = true;
            }
            String source = resultSet.getString("source");
            if (source != null) {
                photoSources.add(source);
            }
        }

        return orderBuilder.photoSources(photoSources).build();
    }
}
