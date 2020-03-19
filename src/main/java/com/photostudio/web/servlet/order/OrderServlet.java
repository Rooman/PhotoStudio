package com.photostudio.web.servlet.order;

import com.photostudio.ServiceLocator;
import com.photostudio.entity.order.Order;
import com.photostudio.entity.order.OrderStatus;
import com.photostudio.service.OrderService;
import com.photostudio.web.templater.TemplateEngineFactory;
import com.photostudio.web.util.CommonVariableAppendService;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@WebServlet(urlPatterns = "/order/*")
@Slf4j
public class OrderServlet extends HttpServlet {
    private OrderService orderService = ServiceLocator.getService(OrderService.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {

        String uri = request.getRequestURI();
        int idFromUri = Integer.parseInt(uri.substring(uri.lastIndexOf("/") + 1));
        String newEmail = request.getParameter("newEmail");

        String errorMessage = (String) request.getSession().getAttribute("errorMessage");

        Map<String, Object> paramsMap = new HashMap<>();
        CommonVariableAppendService.appendUser(paramsMap, request);

        log.info("Request get order page by id:{}", idFromUri);
        log.info("errorMessage:{}", errorMessage);

        Order order = orderService.getOrderByIdInStatusNew(idFromUri);
        request.setAttribute("orderId", idFromUri);

        if (OrderStatus.NEW.equals(order.getStatus())) {
            paramsMap.put("newEmail", newEmail);
        }
        paramsMap.put("order", order);
        paramsMap.put("errorMessage", errorMessage);

        response.setContentType("text/html;charset=utf-8");
        TemplateEngineFactory.process(request, response, "order", paramsMap);
    }
}
