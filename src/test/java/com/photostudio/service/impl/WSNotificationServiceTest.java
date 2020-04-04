package com.photostudio.service.impl;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.photostudio.entity.order.OrderStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class WSNotificationServiceTest {

    @Test
    void testCreateMessage() {
        WSNotificationService notificationService = new WSNotificationService(new ObjectMapper());
        String actualJson = notificationService.createMessage(1, OrderStatus.VIEW_AND_SELECT);

        String expectedJson = "{\"orderId\":1,\"orderStatus\":\"VIEW_AND_SELECT\"}";

        assertEquals(expectedJson, actualJson);
    }
}