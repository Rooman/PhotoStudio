package com.photostudio.web.servlet.user;

import com.photostudio.ServiceLocator;
import com.photostudio.entity.User;
import com.photostudio.service.UserService;
import com.photostudio.web.templater.TemplateEngineFactory;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetAllUsersServlet extends HttpServlet {
    private UserService userService = ServiceLocator.getService(UserService.class);

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<User> users = userService.getAllUsers();

        Map<String, Object> variablesMap = new HashMap<>();
        variablesMap.put("users", users);
        TemplateEngineFactory.process("allusers", variablesMap, response.getWriter());
    }
}
