package com.photostudio.service;

import com.photostudio.entity.order.OrderStatus;

public interface OrderCacheService {
    int getOrderStatusIdByStatusName(OrderStatus status);
}
