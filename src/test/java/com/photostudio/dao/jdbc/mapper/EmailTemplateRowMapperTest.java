package com.photostudio.dao.jdbc.mapper;

import com.photostudio.dao.jdbc.entity.EmailTemplateRow;
import com.photostudio.entity.order.OrderStatus;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


class EmailTemplateRowMapperTest {
    @Test
    void testMapRow() throws SQLException {
        ResultSet mockResultSet = mock(ResultSet.class);

        String subject = "Your Order %d is ready.";
        String body = "You can download selected photos now.";
        when(mockResultSet.getInt("langId")).thenReturn(2);
        when(mockResultSet.getString("statusName")).thenReturn("READY");
        when(mockResultSet.getString("subject")).thenReturn(subject);
        when(mockResultSet.getString("body")).thenReturn(body);

        EmailTemplateRow emailTemplateRow = EmailTemplateRowMapper.mapRow(mockResultSet);

        assertNotNull(emailTemplateRow);

        assertEquals(2, emailTemplateRow.getLangId());
        assertEquals(OrderStatus.READY, emailTemplateRow.getOrderStatus());
        assertEquals("Your Order 1 is ready.", emailTemplateRow.getEmailTemplate().generateHeader(1));
        assertEquals(body, emailTemplateRow.getEmailTemplate().generateBody(1));
    }

}