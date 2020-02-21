package com.photostudio.web.servlet;

import com.photostudio.ServiceLocator;
import com.photostudio.security.SecurityService;
import com.photostudio.web.templater.TemplateEngineFactory;
import com.photostudio.web.util.CommonVariableAppendService;
import com.photostudio.web.util.CookieManager;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AccessDeniedServlet extends HttpServlet {
    private CommonVariableAppendService commonVariableAppendService = new CommonVariableAppendService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        try {
            Map<String, Object> paramsMap = new HashMap<>();

            commonVariableAppendService.appendUser(paramsMap, request);
            response.setContentType("text/html;charset=utf-8");

            TemplateEngineFactory.process("access_denied", paramsMap, response.getWriter());
        } catch (IOException e) {
            throw new RuntimeException("AccessDeniedServlet error", e);
        }
    }
}
