package com.photostudio.web.servlet.order;

import com.photostudio.ServiceLocator;
import com.photostudio.entity.order.Order;
import com.photostudio.entity.order.OrderStatus;
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
import java.util.HashMap;
import java.util.Map;

@WebServlet(urlPatterns = "/order/*")
public class OrderServlet extends HttpServlet {
    private final Logger LOG = LoggerFactory.getLogger(getClass());
    private OrderService defaultOrderService = ServiceLocator.getService(OrderService.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        try {
            int idFromUri = getIdFromUri(request.getRequestURI());

            String newEmail = request.getParameter("newEmail");

            Map<String, Object> paramsMap = new HashMap<>();
            CommonVariableAppendService.appendUser(paramsMap, request);

            LOG.info("Request get order page by id:{} in status NEW", idFromUri);

            Order order = defaultOrderService.getOrderByIdInStatusNew(idFromUri);
            request.setAttribute("orderId", idFromUri);

            if (OrderStatus.NEW.equals(order.getStatus())) {
                paramsMap.put("order", order);
                paramsMap.put("newEmail", newEmail);
                response.setContentType("text/html;charset=utf-8");
                TemplateEngineFactory.process(request, response, "order", paramsMap);
            } else {
                LOG.info("Order with id:{} is not in status New", idFromUri);
                response.sendRedirect(request.getContextPath() + "/orders");
            }

        } catch (IOException e) {
            LOG.error("Get page order in status New error", e);
            throw new RuntimeException("Get page order in status New error", e);
        }
    }

    int getIdFromUri(String uri) {
        String[] partsOfUri = uri.split("/");
        return Integer.parseInt(partsOfUri[partsOfUri.length - 1]);
    }
}
