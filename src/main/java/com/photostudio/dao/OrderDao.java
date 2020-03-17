package com.photostudio.dao;

import com.photostudio.entity.order.FilterParameters;
import com.photostudio.entity.order.Order;
import com.photostudio.entity.order.OrderStatus;

import java.util.List;
import java.util.Map;

public interface OrderDao {
    List<Order> getAll();

    List<Order> getOrdersByParameters(FilterParameters filterParameters);

    List<Order> getOrdersByUserId(long userId);

    Order getOrderByIdInStatusNew(int id);

    void delete(long id);

    int add(Order order, int orderStatusId);

    void savePhotos(Order order, int orderId, List<String> photosPath);

    void changeOrderStatus(long id, boolean forward);

    OrderStatus getOrderStatus(long id);

    int getPhotoCount(long id);

    int getPhotoCountByStatus(long id, int idPhotoStatus);
}
