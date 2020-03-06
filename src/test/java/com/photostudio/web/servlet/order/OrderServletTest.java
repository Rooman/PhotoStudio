package com.photostudio.web.servlet.order;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrderServletTest {
    private OrderServlet orderServlet = new OrderServlet();

    @Test
    void testGetIdFromUri() {
        //prepare
        String uri = "http://localhost:8080/order/47";

        //when
        int id = orderServlet.getIdFromUri(uri);

        //then
        assertEquals(47, id);
    }
}