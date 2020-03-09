package com.photostudio.web.servlet.order;

import com.photostudio.ServiceLocator;
import com.photostudio.entity.order.Order;
import com.photostudio.entity.order.OrderStatus;
import com.photostudio.entity.user.User;
import com.photostudio.service.OrderService;
import com.photostudio.service.UserService;
import com.photostudio.web.templater.TemplateEngineFactory;
import com.photostudio.web.util.CommonVariableAppendService;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(urlPatterns = "/order")
@MultipartConfig
@Slf4j
public class AddNewOrderServlet extends HttpServlet {
    private OrderService orderService = ServiceLocator.getService(OrderService.class);
    private UserService userService = ServiceLocator.getService(UserService.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        String newEmail = request.getParameter("newEmail");
        Map<String, Object> paramsMap = new HashMap<>();
        CommonVariableAppendService.appendUser(paramsMap, request);
        log.info("Request get order page to create a new order");
        Order order = Order.builder().status(OrderStatus.NEW).orderDate(LocalDateTime.now()).build();
        paramsMap.put("order", order);
        paramsMap.put("newEmail", newEmail);
        response.setContentType("text/html;charset=utf-8");
        TemplateEngineFactory.process(request, response, "order", paramsMap);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        log.info("Create new order");
        String email = request.getParameter("email");
        User user = userService.getUserByEmail(email);

        String comment = request.getParameter("comment");

        Order.OrderBuilder orderBuilder = Order.builder()
                .orderDate(LocalDateTime.now())
                .status(OrderStatus.NEW)
                .user(user);

        if (!comment.isEmpty()) {
            orderBuilder.comment(comment);
        }
        log.info("Save photo to new order");

        List<Part> photoToUpload = (List<Part>) request.getParts();//(List<Part>)??????
        int orderId = orderService.add(orderBuilder.build(), photoToUpload);

        response.sendRedirect(request.getContextPath() + "/order/" + orderId);
    }
}
