package com.photostudio.service.impl;

import com.photostudio.ServiceLocator;
import com.photostudio.dao.OrderDao;
import com.photostudio.entity.order.FilterParameters;
import com.photostudio.entity.order.Order;
import com.photostudio.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class DefaultOrderService implements OrderService {
    private final Logger LOG = LoggerFactory.getLogger(getClass());
    private OrderDao orderDao = ServiceLocator.getService(OrderDao.class);

    @Override
    public List<Order> getAll() {
        LOG.info("Started service get all orders from DB");
        return orderDao.getAll();
    }

    @Override
    public List<Order> getOrdersByParameters(FilterParameters filterParameters) {
        LOG.info("Started service get orders by parameters from DB");
        return orderDao.getOrdersByParameters(filterParameters);
    }
}
