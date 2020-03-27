package com.photostudio.web.servlet.user;

import com.photostudio.web.templater.TemplateEngineFactory;
import com.photostudio.web.util.CommonVariableAppendService;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(urlPatterns = "/security/change-password")
@Slf4j
public class ChangePasswordServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.info("Change password form received");
        Map<String, Object> paramsMap = new HashMap<>();
        CommonVariableAppendService.appendUser(paramsMap, request);

        response.setContentType("text/html;charset=utf-8");
        TemplateEngineFactory.process(request, response, "change-password", paramsMap);
    }
}
