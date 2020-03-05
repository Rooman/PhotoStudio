package com.photostudio.dao.file;

import com.photostudio.dao.PhotoDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LocalDiskPhotoDao implements PhotoDao {

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
    public List<String> savePhotoByOrder(List<Part> photos) {
        File uploadFolder = new File(path);
        if (!uploadFolder.exists()) {
            uploadFolder.mkdirs();
        }
        List<String> photosPaths = new ArrayList<>();

        for (Part photo : photos) {
            if (photo != null && photo.getSize() > 0) {
                if (photo.getName().equalsIgnoreCase("photo")) {
                    String fileName = getFileName(photo);
                    String photoPath = uploadFolder + File.separator + fileName;
                    System.out.println("Save photo: " + photoPath);
                    try {
                        photo.write(photoPath);
                        photosPaths.add(photoPath);
                    } catch (IOException e) {//!!!!!!!!!remember+LOG
                        e.printStackTrace();
                    }

                }
            }
        }
        return photosPaths;
    }

    private String getFileName(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        System.out.println("content-disposition header= " + contentDisp);
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
