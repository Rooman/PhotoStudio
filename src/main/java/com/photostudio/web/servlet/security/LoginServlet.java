package com.photostudio.web.servlet.security;

import com.photostudio.ServiceLocator;
import com.photostudio.entity.user.User;
import com.photostudio.exception.LoginPasswordInvalidException;
import com.photostudio.security.SecurityService;
import com.photostudio.security.entity.Session;
import com.photostudio.web.util.CookieManager;
import com.photostudio.web.templater.TemplateEngineFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.photostudio.entity.user.UserRole.ADMIN;


@WebServlet(urlPatterns = "/login")
public class LoginServlet extends HttpServlet {
    private final Logger LOG = LoggerFactory.getLogger(getClass());
    private SecurityService securityService = ServiceLocator.getService(SecurityService.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        LOG.info("Request to login page received");

        Map<String, Object> paramsMap = new HashMap<>();

        response.setContentType("text/html;charset=utf-8");

        TemplateEngineFactory.process(request, response, "login", paramsMap);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        LOG.info("Request with login data received");
        try {
            String login = request.getParameter("login");
            String password = request.getParameter("password");
            try {
                Session session = securityService.login(login, password);

                CookieManager.addCookie(response, "user-token", session.getToken());

                User user = session.getUser();
                if (user == null) {
                    response.sendRedirect(request.getContextPath() + "/");
                } else {
                    LOG.debug(" userRole {}", user.getUserRole().getName());
                    if (ADMIN == user.getUserRole()) {
                        response.sendRedirect(request.getContextPath() + "/admin");
                    } else {
                        response.sendRedirect(request.getContextPath() + "/orders");
                    }
                }

            } catch (LoginPasswordInvalidException e) {
                Map<String, Object> paramsMap = new HashMap<>();
                paramsMap.put("invalid", "yes");
                TemplateEngineFactory.process(request, response, "login", paramsMap);
            }
        } catch (IOException e) {
            LOG.error("LoginServlet doPost error", e);
            throw new RuntimeException("LoginServlet doPost error", e);
        }
    }
}