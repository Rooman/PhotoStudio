package com.photostudio.web.servlet;

import com.photostudio.ServiceLocator;
import com.photostudio.exception.LoginPasswordInvalidException;
import com.photostudio.security.SecurityService;
import com.photostudio.security.entity.Session;
import com.photostudio.web.cookie.CookieManager;
import com.photostudio.web.templater.TemplateEngineFactory;
import com.photostudio.web.util.CommonVariableAppendService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LoginServlet extends HttpServlet {
    private SecurityService securityService = ServiceLocator.getService(SecurityService.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        try {
            Map<String, Object> paramsMap = new HashMap<>();

            new CommonVariableAppendService().appendUser(paramsMap, request);
            response.setContentType("text/html;charset=utf-8");

            TemplateEngineFactory.process("login", paramsMap, response.getWriter());
        } catch (IOException e) {
            throw new RuntimeException("LoginServlet error", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            String login = request.getParameter("login");
            String password = request.getParameter("password");
            try {
                Session session = securityService.login(login, password);

                new CookieManager().addCookie(response, "user-token", session.getToken());

                response.sendRedirect(request.getContextPath() + "/admin");
            } catch (LoginPasswordInvalidException e) {
                Map<String, Object> paramsMap = new HashMap<>();
                paramsMap.put("invalid", "yes");
                TemplateEngineFactory.process("login", paramsMap, response.getWriter());
            }
        } catch (IOException e) {
            throw new RuntimeException("LoginServlet doPost error", e);
        }
    }
}