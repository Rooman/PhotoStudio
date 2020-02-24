package com.photostudio.web.servlet;

import com.photostudio.web.templater.TemplateEngineFactory;
import com.photostudio.web.util.CommonVariableAppendService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HomeServlet extends HttpServlet {
    private final Logger LOG = LoggerFactory.getLogger(getClass());
    private CommonVariableAppendService commonVariableAppendService = new CommonVariableAppendService();

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        LOG.info("Request home page received");
        Map<String, Object> paramsMap = new HashMap<>();
        commonVariableAppendService.appendUser(paramsMap, request);
        response.setContentType("text/html;charset=utf-8");

        response.setStatus(HttpServletResponse.SC_OK);
        TemplateEngineFactory.process("home-page", paramsMap, response.getWriter());
    }
}