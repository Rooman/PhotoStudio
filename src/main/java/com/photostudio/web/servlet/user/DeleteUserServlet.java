package com.photostudio.web.servlet.user;

import com.photostudio.ServiceLocator;
import com.photostudio.service.UserService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DeleteUserServlet extends HttpServlet {
    private static final String ALL_USERS_PAGE = "/admin/users";
    private UserService userService = ServiceLocator.getService(UserService.class);

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        long id = Long.parseLong(request.getParameter("id"));
        userService.delete(id);
        response.sendRedirect(ALL_USERS_PAGE);
    }
}
