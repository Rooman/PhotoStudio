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

@WebServlet(urlPatterns = {"/order", "/order/*"})
public class OrderServlet extends HttpServlet {
    private final Logger LOG = LoggerFactory.getLogger(getClass());
    private OrderService defaultOrderService = ServiceLocator.getService(OrderService.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        try {
            String lastPartOfUri = getLastPartOfUri(request);

            Map<String, Object> paramsMap = new HashMap<>();
            CommonVariableAppendService.appendUser(paramsMap, request);

            if (isNumeric(lastPartOfUri)) {
                int id = Integer.parseInt(lastPartOfUri);
                LOG.info("Request get order page by id:{} in status NEW", id);

                Order order = defaultOrderService.getOrderByIdInStatusNew(id);
                request.setAttribute("orderId", id);

                if (OrderStatus.NEW.equals(order.getStatus())) {
                    paramsMap.put("order", order);
                    response.setContentType("text/html;charset=utf-8");
                    TemplateEngineFactory.process(request, response, "new-order", paramsMap);
                } else {
                    LOG.info("Order with id:{} is not in status New", id);
                    response.sendRedirect(request.getContextPath() + "/orders");
                }
            } else {
                LOG.info("Request get order page for create new order");
                Order order = Order.builder().status(OrderStatus.NEW).orderDate(LocalDateTime.now()).build();
                paramsMap.put("order", order);
                response.setContentType("text/html;charset=utf-8");
                TemplateEngineFactory.process(request, response, "new-order", paramsMap);
            }

        } catch (IOException e) {
            LOG.error("Get page order in status New error", e);
            throw new RuntimeException("Get page order in status New error", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            String lastPartOfUri = getLastPartOfUri(request);

            if (!isNumeric(lastPartOfUri)) {
                LOG.info("Create new order");
                User user = new User();
                user.setEmail(request.getParameter("email"));

                Order order = Order.builder().orderDate(LocalDateTime.now())
                        .status(OrderStatus.NEW)
                        .user(user)
                        .comment(request.getParameter("comment"))
                        .build();

                defaultOrderService.add(order);

            } else {
                LOG.info("Order already exist");
            }
            response.sendRedirect(request.getContextPath() + "/orders");

        } catch (IOException e) {
            LOG.error("Get with order in status New error", e);
            throw new RuntimeException("Get with order in status New error", e);
        }
    }

    private String getLastPartOfUri(HttpServletRequest request) {
        String uri = request.getRequestURI();
        String[] partsOfUri = uri.split("/");
        return partsOfUri[partsOfUri.length - 1];
    }

    private boolean isNumeric(String str) {

        // null or empty
        if (str == null || str.length() == 0) {
            return false;
        }

        return str.chars().allMatch(Character::isDigit);
    }
}
