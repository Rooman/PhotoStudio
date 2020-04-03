package com.photostudio.web.servlet.order;

import com.photostudio.ServiceLocator;
import com.photostudio.entity.user.User;
import com.photostudio.service.OrderService;
import com.photostudio.service.UserService;
import com.photostudio.web.util.UtilClass;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = "/order/edit/*")
@MultipartConfig(maxFileSize = 1024 * 1024 * 50)
@Slf4j
public class EditOrderServlet extends HttpServlet {
    private UserService userService = ServiceLocator.getService(UserService.class);
    private OrderService orderService = ServiceLocator.getService(OrderService.class);

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String uri = request.getRequestURI();
        int orderId = UtilClass.getIdFromPath(uri);

        log.info("Request edit order is received orderId : {}", orderId);

        List<Part> photoToUpload = (List<Part>) request.getParts();

        String email = request.getParameter("email");
        String emailOld = request.getParameter("emailOld");

        String commentAdmin = request.getParameter("commentAdmin");
        String commentAdminOld = request.getParameter("commentAdminOld");

        if (!email.equals(emailOld) || UtilClass.isChanged(commentAdmin, commentAdminOld)) {
            User user = userService.getUserByEmail(email);
            orderService.editOrderByAdmin(orderId, user.getId(), commentAdmin);
        }

        if (!photoToUpload.isEmpty()) {
            orderService.addPhotos(orderId, photoToUpload);
        }

        response.sendRedirect(request.getContextPath() + "/order/" + orderId);
    }


}
