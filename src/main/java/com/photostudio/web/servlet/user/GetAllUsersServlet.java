package com.photostudio.web.servlet.user;

import com.photostudio.ServiceLocator;
import com.photostudio.entity.user.User;
import com.photostudio.service.UserService;
import com.photostudio.web.templater.TemplateEngineFactory;
import com.photostudio.web.util.CommonVariableAppendService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(urlPatterns = "/admin/users")
public class GetAllUsersServlet extends HttpServlet {
    private final Logger LOG = LoggerFactory.getLogger(getClass());

    private UserService userService = ServiceLocator.getService(UserService.class);

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        LOG.info("Request show all users received");
        List<User> users = userService.getAllUsers();

        Map<String, Object> variablesMap = new HashMap<>();
        variablesMap.put("users", users);

        CommonVariableAppendService.appendUser(variablesMap, request);
        CommonVariableAppendService.appendLang(variablesMap, request, response);
        response.setContentType("text/html;charset=utf-8");
        TemplateEngineFactory.process(request, response, "all-users", variablesMap);
    }
}
