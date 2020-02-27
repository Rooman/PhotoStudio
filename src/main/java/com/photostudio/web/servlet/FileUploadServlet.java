package com.photostudio.web.servlet;

import com.photostudio.web.templater.TemplateEngineFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet
@MultipartConfig
public class FileUploadServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {

        try {
            Map<String, Object> paramsMap = new HashMap<>();

            response.setContentType("text/html;charset=utf-8");

            TemplateEngineFactory.process("upload_photos", paramsMap, response.getWriter());
        } catch (IOException e) {

            throw new RuntimeException("Upload photo error", e);
        }
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("helle");
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        // gets absolute path of the web application
        String applicationPath = request.getServletContext().getRealPath("");
        // constructs path of the directory to save uploaded file
        String uploadFilePath = applicationPath + File.separator + 5;
        System.out.println(uploadFilePath);
        // creates upload folder if it does not exists
        File uploadFolder = new File(uploadFilePath);
        if (!uploadFolder.exists()) {
            uploadFolder.mkdirs();
        }

        // write all files in upload folder
        for (Part part : request.getParts()) {
            System.out.println("Part");
            if (part != null && part.getSize() > 0) {
                String fileName = getFileName(part);
                String contentType = part.getContentType();
                System.out.println(fileName);
                // allows only JPEG files to be uploaded
//                if (!contentType.equalsIgnoreCase("image/jpg")) {
//                    continue;
//                }
                part.write(uploadFilePath + File.separator + fileName);
            }
        }
    }

    private String getFileName(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        System.out.println("content-disposition header= "+contentDisp);
        String[] tokens = contentDisp.split(";");
        for (String token : tokens) {
            if (token.trim().startsWith("filename")) {
                return token.substring(token.indexOf("=") + 2, token.length()-1);
            }
        }
        return "";
    }
}
