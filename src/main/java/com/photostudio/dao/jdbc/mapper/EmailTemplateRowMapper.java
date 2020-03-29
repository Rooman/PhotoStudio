package com.photostudio.dao.jdbc.mapper;


import com.photostudio.dao.jdbc.entity.EmailTemplateRow;
import com.photostudio.entity.order.OrderStatus;
import com.photostudio.service.entity.EmailTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;

public class EmailTemplateRowMapper {
    public static EmailTemplateRow mapRow(ResultSet resultSet) throws SQLException {
        int langId = resultSet.getInt("langId");
        OrderStatus orderStatus = OrderStatus.getOrderStatus(resultSet.getString("statusName"));
        String subject = resultSet.getString("subject");
        String body = resultSet.getString("body");
        return new EmailTemplateRow(langId, orderStatus, new EmailTemplate(subject, body));
    }
}
