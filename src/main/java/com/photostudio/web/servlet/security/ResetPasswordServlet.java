package com.photostudio.web.servlet.security;

import com.photostudio.web.templater.TemplateEngineFactory;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@WebServlet(urlPatterns = "/security/reset-password")
@Slf4j
public class ResetPasswordServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        log.info("Reset password form received");

        Map<String, Object> paramsMap = new HashMap<>();

        response.setContentType("text/html;charset=utf-8");
        TemplateEngineFactory.process(request, response, "reset-password", paramsMap);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        log.info("Request with change password received");


    }
}
