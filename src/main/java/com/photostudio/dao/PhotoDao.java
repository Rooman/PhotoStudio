package com.photostudio.dao;

import com.photostudio.dao.entity.PhotoFile;
import com.photostudio.entity.photo.Photo;

import java.io.InputStream;
import java.util.List;

public interface PhotoDao {

    List<String> savePhotoByOrder(List<PhotoFile> photos, int orderId);

    void deleteByOrder(int orderId);

    void deletePhoto(int orderId, String source);

    String getPathToOrderDir(int orderId);

    InputStream addPhotoToArchive(int orderId, List<Photo> photos);

}
