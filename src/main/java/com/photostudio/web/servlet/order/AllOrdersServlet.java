package com.photostudio.web.servlet.order;

import com.photostudio.ServiceLocator;
import com.photostudio.service.OrderService;
import com.photostudio.web.templater.TemplateEngineFactory;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AllOrdersServlet extends HttpServlet {

    private OrderService defaultOrderService = ServiceLocator.getService(OrderService.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        try {
            Map<String, Object> paramsMap = new HashMap<>();

            paramsMap.put("orders", defaultOrderService.getAll());

            TemplateEngineFactory.process("all-orders", paramsMap, response.getWriter());
        } catch (IOException e) {
            throw new RuntimeException("AllOrdersServlet error", e);
        }

    }
}
