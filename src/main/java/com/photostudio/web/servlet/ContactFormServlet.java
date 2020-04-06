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


// This servlet is only required to have access to static pages.
// Probably for "/contact" page there's separate servlet needed, so we'll need to remove "/contact" mapping from here.

@Slf4j
@WebServlet(urlPatterns = {"/contact"})
public class ContactFormServlet extends HttpServlet {
    MailSender mailSender = ServiceLocator.getService(MailSender.class);

    /*@Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String uri = request.getRequestURI();
        log.info("Request change order status is received: order {}", uri);
        String[] partsOfUri = uri.split("/");

        String templateName;

        templateName = partsOfUri[partsOfUri.length - 1];


        log.info("Request home page received");
        Map<String, Object> paramsMap = new HashMap<>();
        CommonVariableAppendService.appendUser(paramsMap, request);

        response.setContentType("text/html;charset=utf-8");

        response.setStatus(HttpServletResponse.SC_OK);
        TemplateEngineFactory.process(request, response, templateName, paramsMap);
    }*/

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String city = request.getParameter("city");
        String index = request.getParameter("index");
        String childGender = request.getParameter("childGender");
        String message = request.getParameter("message");

        StringBuilder stringBuilder = new StringBuilder();
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
        stringBuilder.append(message);

        mailSender.send("New message via contact form", stringBuilder.toString(), "nomarchia2@gmail.com");
    }
}