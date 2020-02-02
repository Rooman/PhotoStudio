package com.photostudio.entity.order;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
@Builder
public final class FilterParameters {
    private LocalDateTime fromDate;
    private LocalDateTime toDate;
    private String email;
    private Long phoneNumber;
    private OrderStatus orderStatus;
}
