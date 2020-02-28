package com.photostudio.web.servlet.order;

import com.photostudio.ServiceLocator;
import com.photostudio.entity.order.Order;
import com.photostudio.entity.order.OrderStatus;
import com.photostudio.entity.user.User;
import com.photostudio.service.OrderService;
import com.photostudio.web.templater.TemplateEngineFactory;
import com.photostudio.web.util.CommonVariableAppendService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@WebServlet(urlPatterns = "/order")
public class AddNewOrderServlet extends HttpServlet {
    private final Logger LOG = LoggerFactory.getLogger(getClass());
    private OrderService defaultOrderService = ServiceLocator.getService(OrderService.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        try {
            Map<String, Object> paramsMap = new HashMap<>();
            CommonVariableAppendService.appendUser(paramsMap, request);
            LOG.info("Request get order page for create new order");
            Order order = Order.builder().status(OrderStatus.NEW).orderDate(LocalDateTime.now()).build();
            paramsMap.put("order", order);
            //Context path needed for redirect in new-order.js
            paramsMap.put("contextPath", request.getContextPath());
            response.setContentType("text/html;charset=utf-8");
            TemplateEngineFactory.process(request, response, "order", paramsMap);
        } catch (IOException e) {
            LOG.error("Get page for new order error", e);
            throw new RuntimeException("Get page for new order error", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        LOG.info("Create new order");
        User user = new User();
        user.setEmail(request.getParameter("email"));

        String comment = request.getParameter("comment");

        Order.OrderBuilder order = Order.builder().orderDate(LocalDateTime.now())
                .status(OrderStatus.NEW)
                .user(user);

        if (comment.equals("")) {
            order.comment(null);
        } else {
            order.comment(comment);
        }

        defaultOrderService.add(order.build());
    }
}
