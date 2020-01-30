package com.photostudio.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
@Builder
public final class Order {
    private int id;
    private String status;
    private LocalDateTime orderDate;
    private String email;
    private long phoneNumber;
    private String comment;
}
