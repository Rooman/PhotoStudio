package com.photostudio.web.servlet.asset;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

public class AssetServlet extends HttpServlet {
    private static final int BUFFER_SIZE = 8192;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        String requestURI = request.getRequestURI().substring(1);
        System.out.println(requestURI);
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(requestURI)) {
            byte[] buffer = new byte[BUFFER_SIZE];
            int numBytesRead;
            while ((numBytesRead = inputStream.read(buffer)) > 0) {
                response.getOutputStream().write(buffer, 0, numBytesRead);
            }
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (IOException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            throw new RuntimeException("Cannot find resource: " + requestURI, e);
        }
    }
}
