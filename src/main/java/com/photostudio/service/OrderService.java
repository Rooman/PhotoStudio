package com.photostudio.service;

import com.photostudio.entity.order.FilterParameters;
import com.photostudio.entity.order.Order;

import javax.servlet.http.Part;
import java.util.List;

public interface OrderService {
    List<Order> getAll();

    List<Order> getOrdersByParameters(FilterParameters filterParameters);

    Order getOrderByIdInStatusNew(int id);

    List<Order> getOrdersByUserId(long userId);

    void delete(long id);

    void add(Order order, List<Part> photoToUpload);

}
