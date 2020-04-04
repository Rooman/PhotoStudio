package com.photostudio.service.entity;

import com.photostudio.entity.order.OrderStatus;
import lombok.Data;

@Data
public class OrderIdAndStatusDto {
    private long orderId;
    private OrderStatus orderStatus;

    public OrderIdAndStatusDto(long orderId, OrderStatus orderStatus) {
        this.orderId = orderId;
        this.orderStatus = orderStatus;
    }
}
