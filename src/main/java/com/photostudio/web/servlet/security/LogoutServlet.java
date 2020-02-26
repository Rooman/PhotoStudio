package com.photostudio.web.servlet.security;

import com.photostudio.ServiceLocator;
import com.photostudio.security.SecurityService;
import com.photostudio.security.entity.Session;
import com.photostudio.web.util.CookieManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LogoutServlet extends HttpServlet {
    private final Logger LOG = LoggerFactory.getLogger(getClass());
    private SecurityService securityService = ServiceLocator.getService(SecurityService.class);

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        LOG.info("Request with logout data received");

        Session session = (Session) request.getAttribute("session");
        securityService.logout(session);

        CookieManager.addCookie(response, "user-token", "");
        LOG.info("User was logged out");
        response.sendRedirect("/login");
    }
}

