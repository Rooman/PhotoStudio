package com.photostudio.dao.file;

import com.photostudio.dao.PhotoDao;
import com.photostudio.entity.photo.Photo;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.Part;
import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

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
        return getOrderPath(orderId).toAbsolutePath().toString();
    }

    @Override
    public void deleteByOrder(int orderId) {
        String orderPath = getPathToOrderDir(orderId);
        log.info("delete photos from local disk by path:{}", orderPath);
        File dir = new File(orderPath);
        if (dir.exists()) {
            if (deleteDir(dir)) {
                log.info("Directory was deleted: {}", orderPath);
            } else {
                log.error("Directory was not deleted : {}", orderPath);
                throw new RuntimeException("Directory was not deleted " + orderPath);
            }
        }
    }

    @Override
    public List<String> savePhotoByOrder(List<Part> photos, int orderId) {
        Path orderPath = getOrderPath(orderId);
        log.info("save photos on local disk by path : {}", orderPath);
        List<String> photosPaths = new ArrayList<>();
        if (!Files.exists(orderPath)) {
            try {
                Files.createDirectory(orderPath);
                log.info("Directory was created : {}", orderPath);
            } catch (IOException e) {
                log.error("Directory was not created : {}", orderPath, e);
                throw new RuntimeException("Directory was not created " + orderPath, e);
            }
        }
        for (Part photo : photos) {
            if (photo != null && photo.getSize() > 0) {
                if (photo.getName().equalsIgnoreCase("photo")) {
                    String fileName = getFileName(photo);
                    Path photoPath = Paths.get(orderPath.toString(), fileName);
                    try (InputStream inputStream = photo.getInputStream()) {
                        Files.copy(inputStream, photoPath, new StandardCopyOption[]{StandardCopyOption.REPLACE_EXISTING});
                        photosPaths.add(fileName);
                    } catch (IOException e) {
                        log.error("Can't save photos on local disk by path : {}", orderPath, e);
                        throw new RuntimeException("Can't save photos on local disk", e);
                    }
                }
            }
        }
        return photosPaths;
    }

    @Override
    public InputStream addPhotoToArchive(int orderId, List<Photo> photos) {
        if(photos.isEmpty()){
            log.error("No paid photos in order with id : {}", orderId);
            throw new RuntimeException ("No paid photos in order with id "+orderId);
        }
        String pathToArchive = getPathToOrderDir(orderId);
        log.info("Add to archive photos on local disk by path : {}", pathToArchive);
        String archiveName = orderId + ".zip";
        File archive = new File(pathToArchive, archiveName);
        try (FileOutputStream fileOutputStream = new FileOutputStream(archive);
             ZipOutputStream zipOutputStream = new ZipOutputStream(fileOutputStream)) {
            for (Photo photo : photos) {
                zipOutputStream.putNextEntry(new ZipEntry(photo.getSource()));
                byte[] buffer = Files.readAllBytes(Paths.get(pathToArchive, photo.getSource()));
                zipOutputStream.write(buffer);
                zipOutputStream.closeEntry();
            }
            return new FileInputStream(archive);
        } catch (FileNotFoundException ex) {
            log.error("A zip file on path : {} does not exist: ", pathToArchive);
            throw new RuntimeException("A zip file does not exist: ", ex);
        } catch (IOException ex) {
            log.error("File for add to zip on path : {} not found", pathToArchive);
            throw new RuntimeException("File for add to zip not found: ", ex);
        }
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

    private Path getOrderPath(int orderId) {
        return Paths.get(path, "Order-" + orderId);
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
