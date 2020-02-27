package com.photostudio.web.servlet;

import com.photostudio.web.templater.TemplateEngineFactory;
import com.photostudio.web.util.CommonVariableAppendService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(urlPatterns = "/admin")
public class AdminPageServlet extends HttpServlet {
    private final Logger LOG = LoggerFactory.getLogger(getClass());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        LOG.info("Request admin page received");
        try {
            Map<String, Object> paramsMap = new HashMap<>();
            CommonVariableAppendService.appendUser(paramsMap, request);
            response.setContentType("text/html;charset=utf-8");
            TemplateEngineFactory.process(request, response, "admin-page", paramsMap);
        } catch (IOException e) {
            LOG.error("AdminPageServlet error", e);
            throw new RuntimeException("AdminPageServlet error", e);
        }
    }
}