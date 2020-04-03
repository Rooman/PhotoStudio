package com.photostudio.web.servlet;

import com.photostudio.ServiceLocator;
import com.photostudio.entity.photo.PhotoStatus;
import com.photostudio.service.OrderService;

import lombok.extern.slf4j.Slf4j;


import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;


@WebServlet(urlPatterns = "/paid/*")
@Slf4j
public class GetPaidPhotosServlet extends HttpServlet {
    private static final int BUFFER_SIZE = 8192;
    private OrderService orderService = ServiceLocator.getService(OrderService.class);


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        log.info("Request to paid photo source received");
        String uri = request.getRequestURI();
        String[] partsOfUri = uri.split("/");
        int orderId = Integer.parseInt(partsOfUri[partsOfUri.length - 1]);

        response.setContentType("application/zip");
        response.setStatus(HttpServletResponse.SC_OK);
        response.addHeader("Content-Disposition", "attachment; filename=" + orderId + ".zip");


        try (InputStream inputStream = orderService.downloadPhotosByStatus(orderId, PhotoStatus.PAID);
             ServletOutputStream outputStream = response.getOutputStream()) {
            int count;
            byte[] buffer = new byte[BUFFER_SIZE];
            while ((count = inputStream.read(buffer)) > -1) {
                outputStream.write(buffer, 0, count);
            }
        } catch (
                FileNotFoundException e) {
            log.error("Paid photo not found", e);
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } catch (
                IOException e) {
            log.error("Loading photo error", e);
            throw new RuntimeException("Loading photo by path error", e);
        }

    }
}
