package com.photostudio.dao;

import com.photostudio.entity.order.FilterParameters;
import com.photostudio.entity.order.Order;

import java.util.List;

public interface OrderDao {
    List<Order> getAll();

    List<Order> getOrdersByParameters(FilterParameters filterParameters);

    List<Order> getOrdersByUserId(long userId);

    Order getOrderByIdInStatusNew(int id);

    void delete(long id);

    int add(Order order);

    void savePhotos(int orderId,List<String> photosPath);
}
