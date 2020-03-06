package com.photostudio.entity.order;

import com.photostudio.entity.user.User;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
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
