package com.photostudio.dao.jdbc.mapper;

import com.photostudio.entity.Order;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderRowMapper {
    public Order mapRow(ResultSet resultSet) throws SQLException {

        return new Order.Builder()
                .withId(resultSet.getInt("id"))
                .withStatus(resultSet.getString("statusName"))
                .withOrderDate(resultSet.getTimestamp("orderDate").toLocalDateTime())
                .withEmail(resultSet.getString("email"))
                .withPhoneNumber(resultSet.getLong("phoneNumber"))
                .withComment(resultSet.getString("comment"))
                .build();
    }
}
