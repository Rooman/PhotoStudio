package com.photostudio.entity.order;

import java.util.Arrays;

public enum OrderStatus {
    NEW("NEW"),
    VIEW_AND_SELECT("VIEW AND SELECT"),
    SELECTED("SELECTED"),
    READY("READY");

    private final String orderStatusName;

    OrderStatus(String orderStatusName) {
        this.orderStatusName = orderStatusName;
    }

    public String getOrderStatusName() {
        return orderStatusName;
    }

    public static OrderStatus getOrderStatus(String orderStatusName) {

        return Arrays.stream(OrderStatus.values())
                .filter(orderStatus -> orderStatus.orderStatusName.equalsIgnoreCase(orderStatusName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("OrderStatus name is not correct!"));
    }
}
