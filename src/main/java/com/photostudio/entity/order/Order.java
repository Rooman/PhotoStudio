package com.photostudio.entity.order;

import com.photostudio.entity.user.User;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Getter
@ToString
@Builder
@EqualsAndHashCode
public final class Order {
    private int id;
    private OrderStatus status;
    private LocalDateTime orderDate;
    private User user;
    private String comment;
    private List<String> photoSources;
}
