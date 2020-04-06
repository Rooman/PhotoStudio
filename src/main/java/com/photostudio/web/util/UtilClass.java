package com.photostudio.web.util;

import com.photostudio.dao.entity.PhotoFile;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;

import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public final class UtilClass {

    public static String getHashedString(String originalString, String salt) {
        String saltPassword = originalString + salt;
        byte[] saltedStringBytes = saltPassword.getBytes();
        return DigestUtils.sha256Hex(saltedStringBytes);
    }

    public static boolean isChanged(String newValue, String oldValue) {
        if (newValue != null) {
            return !newValue.equals(oldValue);
        } else {
            if (oldValue == null) {
                return false;
            } else {
                return true;
            }
        }
    }

    public static int getIdFromPath(String uri) {
        return Integer.parseInt(uri.substring(uri.lastIndexOf("/") + 1));
    }

    public static List<PhotoFile> getListPhotoFiles(List<Part> photos) {
        List<PhotoFile> resultList = new ArrayList<>(photos.size());
        try {
            for (Part photo : photos) {
                if (photo != null && photo.getSize() > 0) {
                    if (photo.getName().equalsIgnoreCase("photo")) {
                        String fileName = getFileName(photo);
                        InputStream inputStream = photo.getInputStream();
                        PhotoFile photoFile = new PhotoFile(fileName, inputStream);
                        resultList.add(photoFile);
                    }
                }
            }
        } catch (IOException ex) {
            log.error("Error during reading files");
            throw new RuntimeException("Error during reading files");
        }
        return resultList;
    }

    private static String getFileName(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        String[] names = contentDisp.split(";");
        for (String name : names) {
            if (name.trim().startsWith("filename")) {
                return name.substring(name.indexOf("=") + 2, name.length() - 1);
            }
        }
        return "";
    }

}
