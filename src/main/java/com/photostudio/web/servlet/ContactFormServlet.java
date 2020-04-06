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

        log.info("Request contact page received");
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("isSent", isSent);
        CommonVariableAppendService.appendUser(paramsMap, request);

        response.setContentType("text/html;charset=utf-8");

        response.setStatus(HttpServletResponse.SC_OK);
        TemplateEngineFactory.process(request, response, "contact", paramsMap);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String name = "Name: " + request.getParameter("name");
        String email = "Email: " + request.getParameter("email");
        String city = "City: " + request.getParameter("city");
        String index = "Index: " + request.getParameter("index");
        String childGender = "Child gender: " + request.getParameter("childGender");
        String message = "Message: " + request.getParameter("message");

        /*StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Name: ");
        stringBuilder.append(name);
        stringBuilder.append("\n");
        stringBuilder.append("Email: ");
        stringBuilder.append(email);
        stringBuilder.append("\n");
        stringBuilder.append("City: ");
        stringBuilder.append(city);
        stringBuilder.append("\n");
        stringBuilder.append("Index: ");
        stringBuilder.append(index);
        stringBuilder.append("\n");
        stringBuilder.append("Child gender: ");
        stringBuilder.append(childGender);
        stringBuilder.append("\n");
        stringBuilder.append("Message: ");
        stringBuilder.append(message);*/

        StringJoiner stringJoiner = new StringJoiner("\n", "", "");
        stringJoiner.add(email);
        stringJoiner.add(city);
        stringJoiner.add(index);
        stringJoiner.add(childGender);
        stringJoiner.add(message);

        mailSender.sendToAdmin("New message via Contact form", stringJoiner.toString());
        response.sendRedirect("contact?isSent=true");
    }
}