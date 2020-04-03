package com.photostudio.dao;

import javax.servlet.http.Part;
import java.io.IOException;
import java.util.List;

public interface PhotoDao {

    String getPathToOrderDir(int orderId);

    List<String> savePhotoByOrder(List<Part> photos, int orderId);

    void deleteByOrder(int orderId);

    void deletePhoto(int orderId, String source);

}
