package com.photostudio.dao;

import com.photostudio.entity.photo.Photo;
import com.photostudio.entity.photo.PhotoStatus;

import javax.servlet.http.Part;
import java.io.InputStream;
import java.util.List;

public interface PhotoDao {

    List<String> saveRetouchedPhotoByOrder(List<Part> photos, int orderId, List<String> photosSources);

    List<String> savePhotoByOrder(List<Part> photos, int orderId);

    void deleteByOrder(int orderId);

    void deletePhoto(int orderId, String source);

    String getPathToOrderDir(int orderId);

    InputStream addPhotoToArchive(int orderId, List<Photo> photos, PhotoStatus photoStatus);

    InputStream downloadRetouchedPhoto(int orderId, String photoSource);

}
