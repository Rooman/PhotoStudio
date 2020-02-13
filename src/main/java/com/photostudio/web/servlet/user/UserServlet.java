package com.photostudio.web.servlet.user;

import com.photostudio.ServiceLocator;
import com.photostudio.entity.user.Gender;
import com.photostudio.entity.user.User;
import com.photostudio.entity.user.UserRole;
import com.photostudio.service.UserService;
import com.photostudio.web.templater.TemplateEngineFactory;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UserServlet extends HttpServlet {
    private UserService userService = ServiceLocator.getService(UserService.class);

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
            response.setContentType("text/html;charset=utf-8");
            TemplateEngineFactory.process("add-user", response.getWriter());
        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String email = request.getParameter("email");
        String phoneNumber = request.getParameter("phoneNumber");
        String gender = request.getParameter("genderName");
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String country = request.getParameter("country");
        String city = request.getParameter("city");
        String zip = request.getParameter("zip");
        String address = request.getParameter("address");
        String password = request.getParameter("password");

        User newUser = new User();

        // mandatory
        newUser.setEmail(email);
        newUser.setUserRole(UserRole.USER);

        // optional
        newUser.setPhoneNumber(phoneNumber);
        newUser.setFirstName(firstName);
        newUser.setLastName(lastName);
        newUser.setCountry(country);
        newUser.setCity(city);
        newUser.setAddress(address);

        if (isNotEmpty(gender)) {
            newUser.setGender(Gender.getByGender(gender));
        }
        //TODO: check 0
        if (isNotEmpty(zip)) {
            newUser.setZip(Integer.parseInt(zip));
        }

        userService.register(newUser, password);

//        response.sendRedirect(request.getContextPath() + "/admin/users");
    }


    public void doDelete(HttpServletRequest request, HttpServletResponse response) {
        String id = request.getParameter("id");
        if (id == null){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        try {
            long userId = Long.parseLong(id);
            userService.delete(userId);

            response.setStatus(HttpServletResponse.SC_OK);
        }catch (Exception e){
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

}
