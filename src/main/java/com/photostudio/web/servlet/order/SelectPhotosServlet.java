package com.photostudio.web.servlet.order;

import com.photostudio.ServiceLocator;
import com.photostudio.entity.order.OrderStatus;
import com.photostudio.entity.user.User;
import com.photostudio.service.OrderService;
import com.photostudio.web.util.CommonVariableAppendService;
import com.photostudio.web.util.UtilClass;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(urlPatterns = "/order/select-photos/*")
@Slf4j
public class SelectPhotosServlet extends HttpServlet {
    private OrderService orderService = ServiceLocator.getService(OrderService.class);

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int orderId = UtilClass.getIdFromPath(request.getRequestURI());
        String selectedPhotos = (String) request.getParameter("selectedPhotos");
        log.info("Request select photos for order {} is received, selected photos {}", orderId, selectedPhotos);

        String commentUser = request.getParameter("commentUser");
        String commentUserOld = request.getParameter("commentUserOld");

        boolean isChanged = UtilClass.isChanged(commentUser, commentUserOld);

        String orderStatusName = request.getParameter("orderStatusName");
        OrderStatus orderStatus = OrderStatus.getOrderStatus(orderStatusName);

        Map<String, Object> paramsMap = new HashMap<>();
        CommonVariableAppendService.appendUser(paramsMap, request);

        User userChanged = (User) paramsMap.get("user");

        orderService.editOrderByUser(orderId, commentUser, userChanged, isChanged, orderStatus, selectedPhotos);
        response.sendRedirect(request.getContextPath() + "/orders");
    }
}
