package com.photostudio.web.servlet;

import com.photostudio.web.templater.TemplateEngineFactory;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AdminPageServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        try {
            Map<String, Object> paramsMap = new HashMap<>();

            TemplateEngineFactory.process("admin-page", paramsMap, response.getWriter());
        } catch (IOException e) {
            throw new RuntimeException("AdminPageServlet error", e);
        }
    }
}