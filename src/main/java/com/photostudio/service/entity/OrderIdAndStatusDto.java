package com.photostudio.service.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderIdAndStatusDto {
    private long orderId;
    private String message;
}
