package com.photostudio.service;

import com.photostudio.entity.order.FilterParameters;
import com.photostudio.entity.order.Order;
import com.photostudio.entity.user.User;

import javax.servlet.http.Part;
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

    int add(Order order, List<Part> photoToUpload);

    void moveStatusForward(int id, User user);

    void moveStatusBack(int id, User user);

    void editOrderByAdmin(Order order, User userChanged, boolean isChanged, List<Part> photoToUpload);

    void editOrderByUser(Order order, User userChanged, boolean isChanged, String selectedPhoto);

    void addPhotos(int orderId, List<Part> photoToUpload);

    String getPathToOrderDir(int orderId);

    String getPathByPhotoId(long photoId);
}
