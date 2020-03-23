package com.photostudio.dao;

import com.photostudio.entity.order.OrderStatus;

public interface OrderStatusDao {
    int getOrderStatusIdByStatusName(OrderStatus status);

    OrderStatus getOrderStatusById(int i);
}
