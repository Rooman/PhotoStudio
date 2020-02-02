package com.photostudio.entity;

import java.util.Arrays;

public enum OrderStatus {
    NEW("New"),
    VIEW_AND_SELECT("View and Select"),
    SELECTED("Selected"),
    READY("Ready");

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
