package com.photostudio.dao;

import javax.servlet.http.Part;
import java.util.List;

public interface PhotoDao {

    void deleteByOrder(long orderId);

    List<String> savePhotoByOrder(List<Part> photos);
}
