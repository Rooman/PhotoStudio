package com.photostudio.service;

import com.photostudio.dao.entity.PhotoFile;
import com.photostudio.entity.order.FilterParameters;
import com.photostudio.entity.order.Order;
import com.photostudio.entity.photo.PhotoStatus;
import com.photostudio.entity.user.User;

import java.io.InputStream;
import java.util.List;

public interface OrderService {

    List<Order> getAll();

    List<Order> getOrdersByParameters(FilterParameters filterParameters);

    Order getOrderById(int id);

    List<Order> getOrdersByUserId(long userId);

    List<Order> getOrdersWithOrderStatusNotNewByUserId(long userId);

    void delete(int id);

    void deleteAllOrdersByUserId(long id);

    void deletePhoto(int orderId, long photoId);

    void deletePhotos(int orderId);

    int add(Order order, List<PhotoFile> photoToUpload);

    void moveStatusForward(int id, User user);

    void moveStatusBack(int id, User user);

    void editOrderByAdmin(Order order, User userChanged, boolean isChanged, List<PhotoFile> photoToUpload);

    void editOrderByUser(Order order, User userChanged, boolean isChanged, String selectedPhoto);

    String getPathToOrderDir(int orderId);

    String getPathByPhotoId(long photoId);

    InputStream downloadPhotosByStatus(int orderId, PhotoStatus photoStatus);
}
