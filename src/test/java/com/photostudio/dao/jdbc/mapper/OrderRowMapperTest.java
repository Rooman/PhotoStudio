package com.photostudio.dao.jdbc.mapper;

import com.photostudio.entity.order.Order;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderRowMapperTest {

    @Test
    public void testMapRow() throws SQLException {
        //prepare
        OrderRowMapper orderRowMapper = new OrderRowMapper();
        ResultSet mockResultSet = mock(ResultSet.class);
        when(mockResultSet.getInt("id")).thenReturn(1);
        when(mockResultSet.getString("statusName")).thenReturn("NEW");
        when(mockResultSet.getLong("phoneNumber")).thenReturn(3806767676L);
        when(mockResultSet.getString("email")).thenReturn("email@gmail.com");
        when(mockResultSet.getString("comment")).thenReturn("new Comment");

        LocalDateTime dateTime = LocalDateTime.of(2020, 1, 20, 1, 20, 0);
        Timestamp sqlDate = Timestamp.valueOf(dateTime);

        when(mockResultSet.getTimestamp("orderDate"))
                .thenReturn(sqlDate);

        //when
        Order actualOrder = orderRowMapper.mapRow(mockResultSet);


        //then
        LocalDateTime expectedDateTime = LocalDateTime.of(2020, 1, 20, 1, 20, 0);

        assertEquals(1, actualOrder.getId());
        assertEquals("NEW", actualOrder.getStatus());
        assertEquals(3806767676L, actualOrder.getPhoneNumber());
        assertEquals("email@gmail.com", actualOrder.getEmail());
        assertEquals("new Comment", actualOrder.getComment());
        assertEquals(expectedDateTime, actualOrder.getOrderDate());

    }
}
