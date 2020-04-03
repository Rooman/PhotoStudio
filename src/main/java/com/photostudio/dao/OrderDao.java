package com.photostudio.dao;

import com.photostudio.entity.order.FilterParameters;
import com.photostudio.entity.order.Order;
import com.photostudio.entity.order.OrderStatus;

import java.util.List;

public interface OrderDao {
    List<Order> getAll();

    List<Order> getOrdersByParameters(FilterParameters filterParameters);

    List<Order> getOrdersByUserId(long userId);

    Order getOrderByIdInStatusNew(int id);

    int add(Order order, int orderStatusId);

    void savePhotos(Order order, int orderId, List<String> photosPath);

    void changeOrderStatus(int id, int orderStatusId);

    OrderStatus getOrderStatus(int id);

    int getPhotoCount(int id);

    int getPhotoCountByStatus(int id, int idPhotoStatus);

    String getPathByPhotoId(long photoId);

    void deleteOrdersByUserId(List<Order> orderList, long id);

    void delete(int id);

    void deletePhoto(long photoId);

    void deletePhotos(int orderId);
}
