package com.photostudio.web.servlet.order;

import com.photostudio.ServiceLocator;
import com.photostudio.entity.user.UserRole;
import com.photostudio.security.SecurityService;
import com.photostudio.security.entity.Session;
import com.photostudio.service.OrderService;
import com.photostudio.web.util.CookieManager;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DeleteOrderServlet extends HttpServlet {

    private OrderService orderService = ServiceLocator.getService(OrderService.class);

    @Override
    public void doPut(HttpServletRequest request, HttpServletResponse response) {
        String id = request.getParameter("id");
        if (id == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        SecurityService securityService = ServiceLocator.getService(SecurityService.class);

        String cookieUserToken = "user-token";

        String token = new CookieManager().getCookie(request, cookieUserToken);
        if (token == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }

        Session session = securityService.getSession(token);
        if (session == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }

        if (!session.getUser().getUserRole().equals(UserRole.ADMIN)) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }

        try {
            long orderId = Long.parseLong(id);

            orderService.delete(orderId);

            response.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
