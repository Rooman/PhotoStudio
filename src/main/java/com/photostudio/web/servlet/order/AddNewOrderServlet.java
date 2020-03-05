package com.photostudio.web.servlet.order;

import com.photostudio.ServiceLocator;
import com.photostudio.entity.order.Order;
import com.photostudio.entity.order.OrderStatus;
import com.photostudio.entity.user.User;
import com.photostudio.service.OrderService;
import com.photostudio.service.UserService;
import com.photostudio.web.templater.TemplateEngineFactory;
import com.photostudio.web.util.CommonVariableAppendService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(urlPatterns = "/order")
@MultipartConfig
public class AddNewOrderServlet extends HttpServlet {
    private final Logger LOG = LoggerFactory.getLogger(getClass());
    private OrderService defaultOrderService = ServiceLocator.getService(OrderService.class);
    private UserService defaultUserService = ServiceLocator.getService(UserService.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        try {
            Map<String, Object> paramsMap = new HashMap<>();
            CommonVariableAppendService.appendUser(paramsMap, request);
            LOG.info("Request get order page for create new order");
            Order order = Order.builder().status(OrderStatus.NEW).orderDate(LocalDateTime.now()).build();
            paramsMap.put("order", order);
            response.setContentType("text/html;charset=utf-8");
            TemplateEngineFactory.process(request, response, "order", paramsMap);
        } catch (IOException e) {
            LOG.error("Get page for new order error", e);
            throw new RuntimeException("Get page for new order error", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        LOG.info("Create new order");
        String email = request.getParameter("email");
        User user = defaultUserService.getUserByLogin(email);

        String comment = request.getParameter("comment");

        Order.OrderBuilder orderBuilder = Order.builder().orderDate(LocalDateTime.now())
                .status(OrderStatus.NEW)
                .user(user);

        if (!comment.isEmpty()) {
            orderBuilder.comment(comment);
        }
        LOG.info("Save photo to new order");

        List<Part> photoToUpload = (List<Part>) request.getParts();//(List<Part>)??????
        defaultOrderService.add(orderBuilder.build(), photoToUpload);

        try {
            response.sendRedirect(request.getContextPath() + "/orders");
        } catch (IOException e) {
            LOG.error("Send redirect after add new order error", e);
            throw new RuntimeException("Send redirect after add new order error", e);
        }
    }
}
