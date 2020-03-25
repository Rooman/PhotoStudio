package com.photostudio.entity.order;

import java.util.Arrays;

public enum OrderStatus {
    NEW("NEW") {
        @Override
        public OrderStatus next() {
            return VIEW_AND_SELECT;
        }

        @Override
        public OrderStatus previous() {
            return null;
        }
    },
    VIEW_AND_SELECT("VIEW AND SELECT") {
        @Override
        public OrderStatus next() {
            return SELECTED;
        }

        @Override
        public OrderStatus previous() {
            return null;
        }
    },
    SELECTED("SELECTED") {
        @Override
        public OrderStatus next() {
            return READY;
        }

        @Override
        public OrderStatus previous() {
            return null;
        }
    },
    READY("READY") {
        @Override
        public OrderStatus next() {
            return null;
        }

        @Override
        public OrderStatus previous() {
            return SELECTED;
        }
    };

    private final String orderStatusName;

    OrderStatus(String orderStatusName) {
        this.orderStatusName = orderStatusName;
    }

    public String getOrderStatusName() {
        return orderStatusName;
    }

    public abstract OrderStatus next();

    public abstract OrderStatus previous();

    public static OrderStatus getOrderStatus(String orderStatusName) {

        return Arrays.stream(OrderStatus.values())
                .filter(orderStatus -> orderStatus.orderStatusName.equalsIgnoreCase(orderStatusName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("OrderStatus name is not correct!"));
    }
}
