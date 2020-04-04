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
@WebServlet(urlPatterns = {"/legal-information", "/data-protection","/portfolio", "/portfolio/*", "/contact", "/price"})
public class StaticPageServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String uri = request.getRequestURI();
        log.info("Request change order status is received: order {}", uri);
        String[] partsOfUri = uri.split("/");

        String templateName;
        if (uri.contains("newborns") || uri.contains("babies")) {
            templateName = partsOfUri[partsOfUri.length - 2];
            templateName += partsOfUri[partsOfUri.length - 1];
        } else {
            templateName = partsOfUri[partsOfUri.length - 1];
        }


        log.info("Request home page received");
        Map<String, Object> paramsMap = new HashMap<>();
        CommonVariableAppendService.appendUser(paramsMap, request);

        response.setContentType("text/html;charset=utf-8");

        response.setStatus(HttpServletResponse.SC_OK);
        TemplateEngineFactory.process(request, response, templateName, paramsMap);
    }
}