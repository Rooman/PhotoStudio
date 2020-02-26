package com.photostudio.web.servlet.order;

import com.photostudio.ServiceLocator;
import com.photostudio.entity.order.Order;
import com.photostudio.entity.order.OrderStatus;
import com.photostudio.service.OrderService;
import com.photostudio.web.templater.TemplateEngineFactory;
import com.photostudio.web.util.CommonVariableAppendService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class OrderServlet extends HttpServlet {
    private final Logger LOG = LoggerFactory.getLogger(getClass());
    private OrderService defaultOrderService = ServiceLocator.getService(OrderService.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        try {
            LOG.info("Request get order page by id in status NEW");
            String uri = request.getRequestURI();
            String[] partsOfUri = uri.split("/");
            int id = Integer.parseInt(partsOfUri[partsOfUri.length - 1]);

            Order order = defaultOrderService.getOrderByIdInStatusNew(id);
            Map<String, Object> paramsMap = new HashMap<>();
            CommonVariableAppendService.appendUser(paramsMap, request);

            if (OrderStatus.NEW.equals(order.getStatus())) {
                paramsMap.put("order", order);
                response.setContentType("text/html;charset=utf-8");
                TemplateEngineFactory.process(request, response, "new-order", paramsMap, response.getWriter());
            } else {
                response.sendRedirect(request.getContextPath() + "/orders");
            }

        } catch (IOException e) {
            LOG.error("Add new order page error", e);
            throw new RuntimeException("Add new order page error", e);
        }
    }
}
