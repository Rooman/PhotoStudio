package com.photostudio.dao.file;

import com.photostudio.dao.PhotoDao;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class LocalDiskPhotoDao implements PhotoDao {
    private String path;

    public LocalDiskPhotoDao(String path) {
        if (path == null) {
            log.error("can't create object LocalDiskPhotoDao: path is null");
            throw new RuntimeException("path to Photo folder is null");
        }
        this.path = path;
    }

    @Override
    public String getPathToOrderDir(int orderId) {
        Path orderDir = Paths.get(path, "Order-" + orderId);
        return orderDir.toAbsolutePath().toString();
    }

    @Override
    public void deleteByOrder(int orderId) {
        String orderPath = getPathToOrderDir(orderId);
        log.info("delete photos from local disk by path:{}", orderPath);
        File dir = new File(orderPath);
        if (dir.exists()) {
            if (deleteDir(dir)) {
                log.info("Directory {} was deleted", orderPath);
            }
        }
    }

    @Override
    public List<String> savePhotoByOrder(List<Part> photos, int orderId) {
        String orderPath = getPathToOrderDir(orderId);
        log.info("save photos on local disk by path : {}", orderPath);
        List<String> photosPaths = new ArrayList<>();
        File dirOrder = new File(orderPath);
        if (!dirOrder.exists()) {
            if (dirOrder.mkdir()) {
                log.info("Directory was created : {}", orderPath);
            }
        }
        for (Part photo : photos) {
            if (photo != null && photo.getSize() > 0) {
                if (photo.getName().equalsIgnoreCase("photo")) {
                    String fileName = getFileName(photo);
                    String photoPath = new File(dirOrder, fileName).getAbsolutePath();
                    try {
                        photo.write(photoPath);
                        photosPaths.add(fileName);
                    } catch (IOException e) {
                        log.error("Can't save photos on local disk by path : {}", orderPath);
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

    private boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            File[] content = dir.listFiles();
            if (content != null) {
                for (File file : content) {
                    deleteDir(file);
                }
            }
        }
        return dir.delete();
    }
}
