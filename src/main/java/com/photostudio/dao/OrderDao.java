package com.photostudio.dao;

import com.photostudio.entity.order.FilterParameters;
import com.photostudio.entity.order.Order;

import java.util.List;

public interface OrderDao {
    List<Order> getAll();

    List<Order> getOrdersByParameters(FilterParameters filterParameters);


    List<Order> getOrdersByUserId(long userId);

    Order getOrderByIdInStatusNew(int id);


    void updateOrderStatusById(long id, long status);


    void delete(long id);

    void addOrderPhotos(long id);

}
