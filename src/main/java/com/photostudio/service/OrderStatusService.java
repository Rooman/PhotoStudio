package com.photostudio.service;

import com.photostudio.entity.order.OrderStatus;

public interface OrderStatusService {
    int getOrderStatusIdByStatusName(OrderStatus status);

    OrderStatus getNext(OrderStatus orderStatus);

    OrderStatus getPrevious(OrderStatus orderStatus);
}
