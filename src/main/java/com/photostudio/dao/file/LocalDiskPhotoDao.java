package com.photostudio.dao.file;

import com.photostudio.dao.PhotoDao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.Part;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class LocalDiskPhotoDao implements PhotoDao {
    private static final int BUFFER_SIZE = 8192;
    private final Logger LOG = LoggerFactory.getLogger(getClass());
    private String path;

    public LocalDiskPhotoDao(String path) {
        if (path == null) {
            LOG.error("can't create object LocalDiskPhotoDao: path is null");
            throw new RuntimeException("path to Photo folder is null");
        }
        this.path = path;
    }

    @Override
    public void deleteByOrder(long orderId) {
        LOG.info("delete photos from local disk by path:{}", path);
        File dir = new File(path, String.valueOf(orderId));
        if (dir.exists()) {
            deleteDir(dir);
        }
        LOG.info("photos for order {} were deleted", orderId);
    }

    @Override
    public List<String> savePhotoByOrder(List<Part> photos, long orderId) {
        LOG.info("save photos on local disk by path : {}", path);
        List<String> photosPaths = new ArrayList<>();
        File dirOrder = new File(path, "Order-" + orderId);
        if (!dirOrder.exists()) {
            dirOrder.mkdir();
        }

        for (Part photo : photos) {
            if (photo != null && photo.getSize() > 0) {
                if (photo.getName().equalsIgnoreCase("photo")) {
                    String fileName = getFileName(photo);
                    String photoPath = new File(dirOrder, fileName).getAbsolutePath();
                    try (InputStream fileContent = photo.getInputStream();
                         FileOutputStream savePhoto = new FileOutputStream(photoPath)) {
                        int count;
                        byte[] buffer = new byte[BUFFER_SIZE];
                        while ((count = fileContent.read(buffer)) != -1) {
                            savePhoto.write(buffer, 0, count);
                        }
                        //photo.write(photoPath);
                        photosPaths.add(photoPath);
                    } catch (IOException e) {
                        LOG.error("Can't save photos on local disk by path : {}", path);
                        throw new RuntimeException("Can't save photos on local disk", e);
                    }
                }
            }
        }
        return photosPaths;
    }

    private String getFileName(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        String[] names = contentDisp.split(";");
        for (String name : names) {
            if (name.trim().startsWith("filename")) {
                return name.substring(name.indexOf("=") + 2, name.length() - 1);
            }
        }
        return "";
    }

    private void deleteDir(File dir) {
        if (dir.isDirectory()) {
            File[] content = dir.listFiles();
            if (content != null) {
                for (File file : content) {
                    deleteDir(file);
                }
            }
        }
        dir.delete();
    }
}
