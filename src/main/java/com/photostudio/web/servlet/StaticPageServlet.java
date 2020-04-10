package com.photostudio.web.servlet;

import com.photostudio.web.templater.TemplateEngineFactory;
import com.photostudio.web.util.CommonVariableAppendService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@WebServlet(urlPatterns = {"/legal-information", "/data-protection","/portfolio", "/portfolio-newborns", "/portfolio-babies", "/price"})
public class StaticPageServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String uri = request.getRequestURI();
        log.info("Request for page received. Uri: {}", uri);
        String[] partsOfUri = uri.split("/");

        String templateName;
        templateName = partsOfUri[partsOfUri.length - 1];

        log.info("Request for page received. Page: {}", templateName);
        Map<String, Object> paramsMap = new HashMap<>();
        CommonVariableAppendService.appendUser(paramsMap, request);

        response.setContentType("text/html;charset=utf-8");

        response.setStatus(HttpServletResponse.SC_OK);
        TemplateEngineFactory.process(request, response, templateName, paramsMap);
    }
}