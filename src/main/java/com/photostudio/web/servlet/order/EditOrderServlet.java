package com.photostudio.web.servlet.order;

import com.photostudio.ServiceLocator;
import com.photostudio.entity.order.Order;
import com.photostudio.entity.order.OrderStatus;
import com.photostudio.entity.user.User;
import com.photostudio.service.OrderService;
import com.photostudio.service.UserService;
import com.photostudio.web.util.CommonVariableAppendService;
import com.photostudio.web.util.UtilClass;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(urlPatterns = "/order/edit/*")
@MultipartConfig(maxFileSize = 1024 * 1024 * 50)
@Slf4j
public class EditOrderServlet extends HttpServlet {
    private UserService userService = ServiceLocator.getService(UserService.class);
    private OrderService orderService = ServiceLocator.getService(OrderService.class);

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String uri = request.getRequestURI();
        int orderId = UtilClass.getIdFromPath(uri);

        log.info("Request edit order is received orderId : {}", orderId);

        List<Part> photoToUpload = (List<Part>) request.getParts();

        String email = request.getParameter("email");
        String emailOld = request.getParameter("emailOld");

        User userOrdered = userService.getUserByEmail(email);

        String commentAdmin = request.getParameter("commentAdmin");
        String commentAdminOld = request.getParameter("commentAdminOld");

        String orderStatusName = request.getParameter("orderStatusName");
        OrderStatus orderStatus = OrderStatus.getOrderStatus(orderStatusName);

        Map<String, Object> paramsMap = new HashMap<>();
        CommonVariableAppendService.appendUser(paramsMap, request);

        User userChanged = (User) paramsMap.get("user");

        Order.OrderBuilder orderBuilder = Order.builder()
                .id(orderId)
                .status(orderStatus)
                .user(userOrdered);

        if (!commentAdmin.isEmpty()) {
            orderBuilder.commentAdmin(commentAdmin);
        }


        boolean orderIsChanged = !email.equals(emailOld) || UtilClass.isChanged(commentAdmin, commentAdminOld);
        orderService.editOrderByAdmin(orderBuilder.build(), userChanged, orderIsChanged, photoToUpload);

        response.sendRedirect(request.getContextPath() + "/order/" + orderId);
    }


}
