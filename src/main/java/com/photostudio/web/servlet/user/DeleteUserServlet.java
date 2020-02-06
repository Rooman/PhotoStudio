package com.photostudio.web.servlet.user;

import com.photostudio.ServiceLocator;
import com.photostudio.service.UserService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DeleteUserServlet extends HttpServlet {
    private UserService userService = ServiceLocator.getService(UserService.class);

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        long id = Long.parseLong(request.getParameter("id"));

        userService.delete(id);

        response.sendRedirect(request.getContextPath() + "/admin/users");
    }
}
