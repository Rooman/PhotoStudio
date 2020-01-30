package com.photostudio.service.impl;

import com.photostudio.ServiceLocator;
import com.photostudio.dao.OrderDao;
import com.photostudio.entity.Order;
import com.photostudio.service.OrderService;

import java.util.List;

public class DefaultOrderService implements OrderService {
    private OrderDao orderDao = ServiceLocator.getService(OrderDao.class);

    @Override
    public List<Order> getAll() {
        return orderDao.getAll();
    }
}
