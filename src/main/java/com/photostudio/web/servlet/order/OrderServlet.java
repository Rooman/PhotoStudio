package com.photostudio.web.servlet.order;

import com.photostudio.ServiceLocator;
import com.photostudio.service.OrderService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class OrderServlet extends HttpServlet {

    private OrderService orderService = ServiceLocator.getService(OrderService.class);

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        String id = request.getParameter("id");
        if (id == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        try {
            long orderId = Long.parseLong(id);

            orderService.delete(orderId);

            response.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
