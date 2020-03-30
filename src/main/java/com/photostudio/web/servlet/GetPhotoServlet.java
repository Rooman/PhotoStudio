package com.photostudio.web.servlet;

import com.photostudio.ServiceLocator;
import com.photostudio.service.OrderService;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

@WebServlet(urlPatterns = "/photo/*")
@Slf4j
public class GetPhotoServlet extends HttpServlet {
    private static final int BUFFER_SIZE = 8192;
    private OrderService orderService = ServiceLocator.getService(OrderService.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        long photoId = Long.parseLong(request.getParameter("id"));
        log.info("Request to photo {} source received", photoId);

        String photoPath = orderService.getPathByPhotoId(photoId);

        log.debug("loading photo {}", photoPath);

        try (InputStream resourceAsStream = new FileInputStream(photoPath);
             BufferedInputStream styleStream = new BufferedInputStream(resourceAsStream)) {
            ServletOutputStream outputStream = response.getOutputStream();

            int count;
            byte[] buffer = new byte[BUFFER_SIZE];
            while ((count = styleStream.read(buffer)) > -1) {
                outputStream.write(buffer, 0, count);
            }
            log.info("loaded photo {}", photoPath);
        } catch (FileNotFoundException e) {
            log.error("Photo not found by path {}", photoPath, e);
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } catch (IOException e) {
            log.error("Loading photo by path {} error", photoPath, e);
            throw new RuntimeException("Loading photo by path: " + photoPath + "error", e);
        }
    }
}
