package com.photostudio.web.servlet;

import com.photostudio.ServiceLocator;
import com.photostudio.entity.photo.PhotoStatus;
import com.photostudio.service.OrderService;

import com.photostudio.web.util.UtilClass;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;


@WebServlet(urlPatterns = "/order/download-paid/*")
@Slf4j
public class DownloadPaidPhotosServlet extends HttpServlet {
    private static final int BUFFER_SIZE = 8192;
    private OrderService orderService = ServiceLocator.getService(OrderService.class);


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        log.info("Request to download photo source received");
        String uri = request.getRequestURI();
        int orderId = UtilClass.getIdFromPath(uri);
        String stringPhotoId = request.getParameter("downloadPhotoId");

        if (stringPhotoId!=null) {
            long photoId = Long.parseLong(request.getParameter("downloadPhotoId"));
            response.setContentType("image/jpg");
            response.setStatus(HttpServletResponse.SC_OK);
            response.addHeader("Content-Disposition", "attachment; filename=" + photoId);
            try (InputStream inputStream = orderService.downloadRetouchedPhoto(orderId, photoId)) {
                downloadPhoto(inputStream, response);
            } catch (IOException e) {
                log.error("Loading photo with id : {} and orderId : {} error", photoId, orderId);
                throw new RuntimeException("Loading photo with id" + photoId + " and orderId " + orderId + " error", e);
            }
        } else {

            response.setContentType("application/zip");
            response.setStatus(HttpServletResponse.SC_OK);
            response.addHeader("Content-Disposition", "attachment; filename=" + orderId + ".zip");

            try (InputStream inputStream = orderService.downloadPhotosByStatus(orderId, PhotoStatus.PAID)) {
                downloadPhoto(inputStream, response);
            } catch (IOException e) {
                log.error("Loading photo error", e);
                throw new RuntimeException("Loading photo by path error", e);
            }
        }

    }

    private void downloadPhoto(InputStream inputStream, HttpServletResponse response) throws IOException {
        ServletOutputStream outputStream = response.getOutputStream();
        int count;
        byte[] buffer = new byte[BUFFER_SIZE];
        while ((count = inputStream.read(buffer)) > -1) {
            outputStream.write(buffer, 0, count);
        }
    }

}