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

        String uri = request.getRequestURI();
        String[] partsOfUri = uri.split("/");
        long orderId = Long.parseLong(partsOfUri[partsOfUri.length - 1]);

        LOG.info("Request delete order is received: order {}", orderId);

        try {
            orderService.delete(orderId);
            response.setStatus(HttpServletResponse.SC_OK);
            response.sendRedirect(partsOfUri[0] + "/orders");
        } catch (Exception e) {
            LOG.error("Error in the request for delete order", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            throw new RuntimeException("Error trying to delete user", e);
        }
    }
}
