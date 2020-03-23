package com.photostudio.exception;

import com.photostudio.entity.order.OrderStatus;
import com.photostudio.exception.entity.ChangeOrderStatusError;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ChangeOrderStatusInvalidException extends RuntimeException {

    private String message;

    public ChangeOrderStatusInvalidException(ChangeOrderStatusError error) {
        super();
        this.message = error.getMessage();
        log.error(this.message);
    }

    public ChangeOrderStatusInvalidException(ChangeOrderStatusError error, OrderStatus orderStatus) {
        super();
        this.message = String.format(error.getMessage(), orderStatus.getOrderStatusName());
        log.error(this.message);
    }

    @Override
    public String getMessage() {
        return message;
    }
}
