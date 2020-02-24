package com.photostudio.web.servlet.order;

import com.photostudio.ServiceLocator;
import com.photostudio.entity.user.UserRole;
import com.photostudio.security.SecurityService;
import com.photostudio.security.entity.Session;
import com.photostudio.service.OrderService;
import com.photostudio.web.util.CookieManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DeleteOrderServlet extends HttpServlet {
    private final Logger LOG = LoggerFactory.getLogger(getClass());
    private OrderService orderService = ServiceLocator.getService(OrderService.class);

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) {

        LOG.info("Request delete order is received");
        String id = request.getParameter("id");
        LOG.debug("order {id}", id);
        if (id == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        SecurityService securityService = ServiceLocator.getService(SecurityService.class);

        String cookieUserToken = "user-token";

        String token = new CookieManager().getCookie(request, cookieUserToken);
        if (token == null) {
            LOG.error("token is not found");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        Session session = securityService.getSession(token);
        if (session == null) {
            LOG.error("session for token is not found");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        if (!session.getUser().getUserRole().equals(UserRole.ADMIN)) {
            LOG.error("user is not Admin");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
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
