package com.photostudio.web.servlet.order;

import com.photostudio.ServiceLocator;
import com.photostudio.entity.user.User;
import com.photostudio.exception.ChangeOrderStatusInvalidException;
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

@WebServlet(urlPatterns = "/order/forward/*")
@Slf4j
public class ChangeOrderStatusServlet extends HttpServlet {
    private OrderService orderService = ServiceLocator.getService(OrderService.class);

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        String uri = request.getRequestURI();
        int orderId = UtilClass.getIdFromPath(uri);

        log.info("Request change order status is received: order {}", orderId);

        Map<String, Object> paramsMap = new HashMap<>();
        CommonVariableAppendService.appendUser(paramsMap, request);
        response.setContentType("text/html;charset=utf-8");

        User user = (User) paramsMap.get("user");

        try {
            orderService.moveStatusForward(orderId, user);
            response.setStatus(HttpServletResponse.SC_OK);
            response.sendRedirect(request.getContextPath() + "/orders");
        } catch (ChangeOrderStatusInvalidException ex) {
            log.error(ex.getLocalizedMessage());
            request.getSession().setAttribute("errorMessage", ex.getLocalizedMessage());
            String path = request.getContextPath() + "/order/" + orderId;
            log.info("go to {}", path);
            response.sendRedirect(path);
        } catch (Exception e) {
            log.error("Error in the request for change order status", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            throw new RuntimeException("Error trying to change order status", e);
        }
    }
}
