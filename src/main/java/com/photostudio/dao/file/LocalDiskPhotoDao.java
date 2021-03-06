package com.photostudio.dao.file;

import com.photostudio.dao.PhotoDao;
import com.photostudio.entity.photo.Photo;
import com.photostudio.entity.photo.PhotoStatus;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;

import javax.imageio.ImageIO;
import javax.servlet.http.Part;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Slf4j
public class LocalDiskPhotoDao implements PhotoDao {
    private static final int BUFFER_SIZE = 8192;
    private String path;
    private String pathToWatermark;
    private int sizePreviewPhoto = 800;

    public LocalDiskPhotoDao(String path) {
        if (path == null) {
            log.error("can't create object LocalDiskPhotoDao: path is null");
            throw new RuntimeException("path to Photo folder is null");
        }
        this.path = path;
    }

    public LocalDiskPhotoDao(String path, String pathToWatermark, int sizePreviewPhoto) {
        if (path == null) {
            log.error("can't create object LocalDiskPhotoDao: path is null");
            throw new RuntimeException("path to Photo folder is null");
        }
        if (pathToWatermark == null) {
            log.error("can't create object LocalDiskPhotoDao: path to watermark is null");
            throw new RuntimeException("path to watermark is null");
        }
        if (sizePreviewPhoto != 0){
            this.sizePreviewPhoto = sizePreviewPhoto;
        }
        this.path = path;
        this.pathToWatermark = pathToWatermark;

    }

    @Override
    public String getPathToOrderDir(int orderId) {
        return getPathToPreviewPhoto(orderId).toAbsolutePath().toString();
    }

    @Override
    public void deletePhoto(int orderId, String source) {
        log.info("delete file {} from order {}", source, orderId);
        Path photoPath = Paths.get(getPathToOrderDir(orderId), source);
        Path originalPhotoPath = Paths.get(getOrderPath(orderId).toString(), source);
        Path retouchedPhotoPath = Paths.get(getPathToRetouchedPhoto(orderId).toString(), source);
        try {
            if (!Files.deleteIfExists(photoPath)) {
                throw new RuntimeException("File " + source + " does not exist");
            }
            Files.deleteIfExists(originalPhotoPath);
            Files.deleteIfExists(retouchedPhotoPath);
        } catch (IOException e) {
            log.error("File was not deleted : {}", photoPath, e);
            throw new RuntimeException("File was not deleted " + photoPath, e);
        }
    }

    @Override
    public void deleteByOrder(int orderId) {
        String orderPath = getPathToOrder(orderId).toString();
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
        Path previewPhotoDir = getPathToPreviewPhoto(orderId);
        Path retouchedPhotoDir = getPathToRetouchedPhoto(orderId);
        log.info("save photos on local disk by path : {}", orderPath);
        List<String> photosPaths = new ArrayList<>();
        createDirectoryForPhoto(orderPath);
        createDirectoryForPhoto(previewPhotoDir);
        createDirectoryForPhoto(retouchedPhotoDir);
        for (Part photo : photos) {
            if (photo != null && photo.getSize() > 0) {
                if (photo.getName().equalsIgnoreCase("photo")) {
                    String fileName = getFileName(photo);
                    Path photoPath = Paths.get(orderPath.toString(), fileName);
                    Path previewPhotoPath = Paths.get(previewPhotoDir.toString(), fileName);
                    uploadPhoto(photoPath, photo);
                    resizePhotoAddWatermark(photoPath.toString(), previewPhotoPath.toString());
                    photosPaths.add(fileName);
                }
            }
        }
        return photosPaths;
    }

    @Override
    public List<String> saveRetouchedPhotoByOrder(List<Part> photosParts, int orderId, List<String> selectedSources) {
        String orderPath = getPathToOrderDir(orderId);
        log.info("save retouched photos on local disk by path : {}", orderPath);
        List<String> retouchedPhotosPaths = new ArrayList<>();
        Path retouchedPhotosPath = getPathToRetouchedPhoto(orderId);
        createDirectoryForPhoto(retouchedPhotosPath);
        for (Part photo : photosParts) {
            if (photo != null && photo.getSize() > 0) {
                if (photo.getName().equalsIgnoreCase("photo")) {
                    String fileName = getFileName(photo);
                    if (selectedSources.contains(fileName)) {
                        Path photoPath = Paths.get(retouchedPhotosPath.toString(), fileName);
                        uploadPhoto(photoPath, photo);
                        retouchedPhotosPaths.add(fileName);
                    }
                }
            }
        }
        return retouchedPhotosPaths;
    }

    private void uploadPhoto(Path photoPath, Part photo) {
        log.error("Upload photos on local disk by path : {}", photoPath);
        try (InputStream inputStream = photo.getInputStream()) {
            Files.copy(inputStream, photoPath, new StandardCopyOption[]{StandardCopyOption.REPLACE_EXISTING});
        } catch (IOException e) {
            log.error("Can't save photos on local disk by path : {}", photoPath, e);
            throw new RuntimeException("Can't save photos on local disk", e);
        }
    }

    private void createDirectoryForPhoto(Path directory) {
        if (!Files.exists(directory)) {
            try {
                Files.createDirectories(directory);
                log.info("Directory was created : {}", directory);
            } catch (IOException e) {
                log.error("Directory was not created : {}", directory, e);
                throw new RuntimeException("Directory was not created " + directory, e);
            }
        }
    }

    @Override
    public InputStream downloadRetouchedPhoto(int orderId, String photoSource){
        log.info("Download retouched photo with source : {} and orderId : {} ", photoSource, orderId);
        try {
            return new FileInputStream(new File (getPathToRetouchedPhoto(orderId).toString(), photoSource));
        } catch (FileNotFoundException e) {
            log.error("Retouched photo with source : {} and orderId : {} not found", photoSource, orderId);
            throw  new RuntimeException("Retouched photo with source " + photoSource + " and orderId " + orderId + " not found", e);
        }
    }

    @Override
    public InputStream addPhotoToArchive(int orderId, List<Photo> photos, PhotoStatus photoStatus) {
        String pathToOrderPhoto;
        if (photoStatus.equals(PhotoStatus.PAID)) {
            pathToOrderPhoto = getPathToRetouchedPhoto(orderId).toString();
        } else {
            pathToOrderPhoto = getOrderPath(orderId).toString();
        }
        log.info("Add to archive photos on local disk by path : {}", pathToOrderPhoto);
        String archiveName = orderId + ".zip";
        File archive = new File(pathToOrderPhoto, archiveName);
        try (FileOutputStream fileOutputStream = new FileOutputStream(archive);
             ZipOutputStream zipOutputStream = new ZipOutputStream(fileOutputStream)) {
            int count;
            byte[] buffer = new byte[BUFFER_SIZE];
            for (Photo photo : photos) {
                File pathToPhoto = new File(pathToOrderPhoto, photo.getSource());
                try (FileInputStream filePhotoInputStream = new FileInputStream(pathToPhoto)) {
                    zipOutputStream.putNextEntry(new ZipEntry(photo.getSource()));
                    while ((count = filePhotoInputStream.read(buffer)) > -1) {
                        zipOutputStream.write(buffer, 0, count);
                    }
                    zipOutputStream.closeEntry();
                }
            }
            return new FileInputStream(archive);
        } catch (FileNotFoundException ex) {
            log.error("A zip file on path : {} does not exist: ", pathToOrderPhoto);
            throw new RuntimeException("A zip file does not exist: ", ex);
        } catch (IOException ex) {
            log.error("File for add to zip on path : {} not found", pathToOrderPhoto);
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

    private void resizePhotoAddWatermark(String fromPhoto, String toPhoto) {
        log.info("Resize photo : {}", fromPhoto);
        try {
            BufferedImage originalImage = ImageIO.read(new File(fromPhoto));
            BufferedImage watermarkImage = ImageIO.read(new File(pathToWatermark));
            Thumbnails.of(originalImage)
                    .size(sizePreviewPhoto, sizePreviewPhoto)
                    .watermark(Positions.BOTTOM_RIGHT, watermarkImage, 0.5f)
                    .toFile(toPhoto);
            log.info("Save resizing photo : {}", toPhoto);
        } catch (IOException e) {
            log.error("Cannot read photo to resize", e);
            throw new RuntimeException("Cannot read photo to resize", e);
        }
    }

    private Path getOrderPath(int orderId) {
        return Paths.get(path, "Order-" + orderId, "original");
    }

    private Path getPathToRetouchedPhoto(int orderId) {
        return Paths.get(path, "Order-" + orderId, "final");
    }

    private Path getPathToPreviewPhoto(int orderId) {
        return Paths.get(path, "Order-" + orderId, "preview");
    }

    private Path getPathToOrder(int orderId) {
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
