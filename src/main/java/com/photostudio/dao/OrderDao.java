package com.photostudio.dao;

import com.photostudio.entity.FilterParameters;
import com.photostudio.entity.Order;

import java.util.List;

public interface OrderDao {
    List<Order> getAll();

    List<Order> getOrdersByParameters(FilterParameters filterParameters);
}
