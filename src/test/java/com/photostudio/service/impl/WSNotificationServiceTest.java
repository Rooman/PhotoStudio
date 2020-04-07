package com.photostudio.service.impl;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.photostudio.service.entity.OrderIdAndMessageText;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class WSNotificationServiceTest {

    @Test
    void testCreateMessage() {
        WSNotificationService webNotificationService = new WSNotificationService(new ObjectMapper());
        String actualJson = webNotificationService.createMessage(new OrderIdAndMessageText(1, "Your order is ready"));

        String expectedJson = "{\"orderId\":1,\"message\":\"Your order is ready\"}";

        assertEquals(expectedJson, actualJson);
    }
}