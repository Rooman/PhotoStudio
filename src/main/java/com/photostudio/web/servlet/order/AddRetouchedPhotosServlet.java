package com.photostudio.web.servlet.order;

import com.photostudio.ServiceLocator;
import com.photostudio.service.OrderService;
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

@WebServlet(urlPatterns = "/order/upload-retouched/*")
@MultipartConfig(maxFileSize = 1024 * 1024 * 50)
@Slf4j
public class AddRetouchedPhotosServlet extends HttpServlet {
    private OrderService orderService = ServiceLocator.getService(OrderService.class);

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String uri = request.getRequestURI();
        int orderId = UtilClass.getIdFromPath(uri);

        log.info("Request upload retouched photo to order with orderId : {}", orderId);

        List<Part> photoToUpload = (List<Part>) request.getParts();
        orderService.addRetouchedPhotos(orderId,photoToUpload);
        response.sendRedirect(request.getContextPath() + "/order/" + orderId);

    }
}
