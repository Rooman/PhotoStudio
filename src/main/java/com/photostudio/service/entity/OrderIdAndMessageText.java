package com.photostudio.service.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderIdAndMessageText {
    private long orderId;
    private String message;
}
