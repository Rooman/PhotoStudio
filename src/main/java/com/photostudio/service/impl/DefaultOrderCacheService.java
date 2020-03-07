package com.photostudio.service.impl;

import com.photostudio.ServiceLocator;
import com.photostudio.dao.OrderStatusDao;
import com.photostudio.entity.order.OrderStatus;
import com.photostudio.service.OrderCacheService;

public class DefaultOrderCacheService implements OrderCacheService {
    private OrderStatusDao orderStatusDao = ServiceLocator.getService(OrderStatusDao.class);

    @Override
    public int getOrderStatusIdByStatusName(OrderStatus status) {
        return orderStatusDao.getOrderStatusIdByStatusName(status);
    }
}
