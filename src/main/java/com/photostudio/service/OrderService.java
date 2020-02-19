package com.photostudio.service;

import com.photostudio.entity.order.FilterParameters;
import com.photostudio.entity.order.Order;

import java.util.List;

public interface OrderService {
    List<Order> getAll();

    List<Order> getOrdersByParameters(FilterParameters filterParameters);
}