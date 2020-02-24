package com.photostudio.web.servlet.security;

import com.photostudio.ServiceLocator;
import com.photostudio.security.SecurityService;
import com.photostudio.security.entity.Session;
import com.photostudio.web.util.CommonVariableAppendService;
import com.photostudio.web.util.CookieManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LogoutServlet extends HttpServlet {
    private final Logger LOG = LoggerFactory.getLogger(getClass());
    private SecurityService securityService = ServiceLocator.getService(SecurityService.class);

    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        LOG.info("Request with logout data received");
        try {
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals("user-token")) {
                        String token = cookie.getValue();
                        Session session = securityService.getSession(token);

                        securityService.logout(session);
                    }
                }
            }
            response.sendRedirect("/login");
        } catch (IOException e) {
            throw new RuntimeException("Logout error", e);
        }
    }
}

