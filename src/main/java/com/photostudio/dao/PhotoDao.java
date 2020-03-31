package com.photostudio.dao;

import javax.servlet.http.Part;
import java.util.List;

public interface PhotoDao {

    void deleteByOrder(int orderId);

    List<String> savePhotoByOrder(List<Part> photos,int orderId);

    String getPathToOrderDir(int orderId);
}
