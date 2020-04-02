package com.photostudio.dao;

import com.photostudio.entity.photo.Photo;

import javax.servlet.http.Part;
import java.io.InputStream;
import java.util.List;

public interface PhotoDao {

    void deleteByOrder(int orderId);

    List<String> savePhotoByOrder(List<Part> photos,int orderId);

    String getPathToOrderDir(int orderId);

    InputStream addPhotoToArchive(int orderId, List<Photo> photos);
}
