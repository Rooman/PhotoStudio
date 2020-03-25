package com.photostudio.exception;

import com.photostudio.entity.order.OrderStatus;
import com.photostudio.exception.entity.ErrorChangeOrderStatus;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ChangeOrderStatusInvalidException extends RuntimeException {

    private String message;

    public ChangeOrderStatusInvalidException(ErrorChangeOrderStatus error) {
        super();
        this.message = error.getMessage();
    }

    public ChangeOrderStatusInvalidException(ErrorChangeOrderStatus error, OrderStatus orderStatus) {
        super();
        this.message = String.format(error.getMessage(), orderStatus.getOrderStatusName());
    }

    @Override
    public String getMessage() {
        return message;
    }
}
