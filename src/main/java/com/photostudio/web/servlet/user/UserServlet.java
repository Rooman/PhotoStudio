package com.photostudio.web.servlet.user;

import com.photostudio.ServiceLocator;
import com.photostudio.entity.user.User;
import com.photostudio.service.UserService;
import com.photostudio.web.templater.TemplateEngineFactory;
import com.photostudio.web.util.CommonVariableAppendService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UserServlet extends HttpServlet {
    private UserService userService = ServiceLocator.getService(UserService.class);
    private CommonVariableAppendService commonVariableAppendService = new CommonVariableAppendService();

    private static boolean isNotEmpty(String value) {
        return value != null && !value.isEmpty();
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (isNotEmpty(request.getParameter("id"))) {
            long userId = Long.parseLong(request.getParameter("id"));

            User user = userService.getUserById(userId);
            Map<String, Object> paramsMap = new HashMap<>();
            paramsMap.put("user", user);

            response.setContentType("text/html;charset=utf-8");
            response.setStatus(HttpServletResponse.SC_OK);
            TemplateEngineFactory.process("user-info", paramsMap, response.getWriter());
        } else {
            Map<String, Object> paramsMap = new HashMap<>();
            commonVariableAppendService.appendUser(paramsMap, request);
            response.setContentType("text/html;charset=utf-8");
            TemplateEngineFactory.process("add-user", paramsMap, response.getWriter());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String email = request.getParameter("email");
        String phoneNumber = request.getParameter("phoneNumber");
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String country = request.getParameter("country");
        String city = request.getParameter("city");
        String zip = request.getParameter("zipCode");
        String address = request.getParameter("address");
        String title = request.getParameter("title");
        String additionalInfo = request.getParameter("additionalInfo");
        String password = request.getParameter("password");

        User newUser = new User();

        newUser.setEmail(email);
        newUser.setPhoneNumber(phoneNumber);
        newUser.setFirstName(firstName);
        newUser.setLastName(lastName);
        newUser.setCountry(country);
        newUser.setCity(city);
        newUser.setZip(zip==null?0:Integer.parseInt(zip));
        newUser.setAddress(address);
        newUser.setTitle(title);
        newUser.setAdditionalInfo(additionalInfo);
        newUser.setPasswordHash(password);
        newUser.setSalt("");



        userService.add(newUser);
        response.sendRedirect("/admin/users");
    }

    public void doDelete(HttpServletRequest request, HttpServletResponse response) {
        String id = request.getParameter("id");
        if (id == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        try {
            long userId = Long.parseLong(id);
            userService.delete(userId);

            response.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}