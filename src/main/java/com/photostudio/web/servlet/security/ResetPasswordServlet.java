package com.photostudio.web.servlet.security;

import com.photostudio.ServiceLocator;
import com.photostudio.entity.user.User;
import com.photostudio.security.SecurityService;
import com.photostudio.service.UserService;
import com.photostudio.web.templater.TemplateEngineFactory;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(urlPatterns = "/security/reset-password")
@Slf4j
public class ResetPasswordServlet extends HttpServlet {
    private SecurityService securityService = ServiceLocator.getService(SecurityService.class);
    private UserService userService = ServiceLocator.getService(UserService.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        log.info("Reset password form received");

        Map<String, Object> paramsMap = new HashMap<>();

        response.setContentType("text/html;charset=utf-8");
        TemplateEngineFactory.process(request, response, "reset-password", paramsMap);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info("Request with reset password received");

        String email = request.getParameter("email");

        User user = userService.getUserByEmail(email);

        if (user != null) {
            log.info("User with email {} exist", email);
            securityService.resetUserPassword(user);
            response.sendRedirect(request.getContextPath() + "/login");
        } else {
            log.info("User with email {} does not exist", email);
            Map<String, Object> paramsMap = new HashMap<>();
            paramsMap.put("invalid", "yes");
            response.setContentType("text/html;charset=utf-8");
            TemplateEngineFactory.process(request, response, "reset-password", paramsMap);
        }
    }
}
