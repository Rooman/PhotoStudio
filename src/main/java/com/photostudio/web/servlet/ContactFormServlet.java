package com.photostudio.web.servlet;

import com.photostudio.ServiceLocator;
import com.photostudio.web.templater.TemplateEngineFactory;
import com.photostudio.web.util.CommonVariableAppendService;
import com.photostudio.web.util.MailSender;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

@Slf4j
@WebServlet(urlPatterns = {"/contact"})
public class ContactFormServlet extends HttpServlet {
    MailSender mailSender = ServiceLocator.getService(MailSender.class);

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Boolean isSent = Boolean.parseBoolean(request.getParameter("isSent"));

        log.info("Request for contact page received");
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("isSent", isSent);
        CommonVariableAppendService.appendUser(paramsMap, request);

        response.setContentType("text/html;charset=utf-8");

        response.setStatus(HttpServletResponse.SC_OK);
        TemplateEngineFactory.process(request, response, "contact", paramsMap);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String name =  request.getParameter("name");
        String email = request.getParameter("email");
        String city = request.getParameter("city");
        String index = request.getParameter("index");
        String childGender = request.getParameter("childGender");
        String message = request.getParameter("message");

        String[] fieldNames = {"Name: ", "Email: ", "City: ", "Index: ", "Child gender: ", "Message: "};
        String[] values = {name, email, city, index, childGender, message};

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < fieldNames.length; i++) {
            stringBuilder.append(fieldNames[i]);
            stringBuilder.append(values[i]);
            stringBuilder.append("\n");
        }

        mailSender.sendToAdmin("New message via Contact form", stringBuilder.toString());
        response.sendRedirect("contact?isSent=true");
    }
}