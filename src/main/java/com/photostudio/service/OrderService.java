package com.photostudio.service;

import com.photostudio.entity.order.FilterParameters;
import com.photostudio.entity.order.Order;
import com.photostudio.entity.user.User;

import javax.servlet.http.Part;
import java.util.List;

public interface OrderService {

    List<Order> getAll();

    List<Order> getOrdersByParameters(FilterParameters filterParameters);

    Order getOrderByIdInStatusNew(int id);

    List<Order> getOrdersByUserId(long userId);

    void delete(int id);

    int add(Order order, List<Part> photoToUpload);

    void moveStatusForward(int id, User user);

    void moveStatusBack(int id, User user);

    void deleteAllOrdersByUserId(long id);
}
