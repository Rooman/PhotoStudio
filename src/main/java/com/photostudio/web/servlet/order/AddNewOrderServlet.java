package com.photostudio.web.servlet.order;

import com.photostudio.ServiceLocator;
import com.photostudio.entity.order.Order;
import com.photostudio.entity.order.OrderStatus;
import com.photostudio.entity.user.User;
import com.photostudio.exception.GetUserByEmailException;
import com.photostudio.service.OrderService;
import com.photostudio.service.UserService;
import com.photostudio.util.PropertyReader;
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
@MultipartConfig(maxFileSize = 1024 * 1024 * 50)
@Slf4j
public class AddNewOrderServlet extends HttpServlet {
    private OrderService orderService = ServiceLocator.getService(OrderService.class);
    private UserService userService = ServiceLocator.getService(UserService.class);
    private PropertyReader propertyReader = ServiceLocator.getService(PropertyReader.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        String newEmail = request.getParameter("newEmail");
        log.info("Request get order page to create a new order");



        Map<String, Object> paramsMap = getParametersMap(request, newEmail);

        // add current user to thymeleaf parameter map
        CommonVariableAppendService.appendUser(paramsMap, request);

        response.setContentType("text/html;charset=utf-8");
        TemplateEngineFactory.process(request, response, "order", paramsMap);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        log.info("Create new order");

        // findbugs!!!
        List<Part> photoToUpload = (List<Part>) request.getParts();

        String email = request.getParameter("email");
        User user = userService.getUserByEmail(email);

        if (user == null) {
            log.error("User with email {} doesn't found", email);
            Map<String, Object> paramsMap = getParametersMap(request, email);

            paramsMap.put("invalid", "yes");
            response.setContentType("text/html;charset=utf-8");
            TemplateEngineFactory.process(request, response, "order", paramsMap);
        } else {
            String comment = request.getParameter("commentAdmin");

            Order.OrderBuilder orderBuilder = Order.builder()
                    .orderDate(LocalDateTime.now())
                    .status(OrderStatus.NEW)
                    .user(user);

            if (!comment.isEmpty()) {
                orderBuilder.commentAdmin(comment);
            }
            log.info("Save photo to new order");

            int orderId = orderService.add(orderBuilder.build(), photoToUpload);

            response.sendRedirect(request.getContextPath() + "/order/" + orderId);
        }
    }

    private Map<String, Object> getParametersMap(HttpServletRequest request, String email) {
        Map<String, Object> paramsMap = new HashMap<>();

        CommonVariableAppendService.appendUser(paramsMap, request);
        Order order = Order.builder().status(OrderStatus.NEW).orderDate(LocalDateTime.now()).build();
        paramsMap.put("order", order);
        paramsMap.put("newEmail", email);
        paramsMap.put("acceptedFileTypes", propertyReader.getString("order.photo.fileType"));
        return paramsMap;
    }
}
