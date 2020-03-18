package com.photostudio.entity.order;

import com.photostudio.entity.user.User;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
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

    private Order(Order order, List<String> sources) {
        this.id = order.id;
        this.status = order.status;
        this.orderDate = order.orderDate;
        this.user = order.user;
        this.comment = order.comment;
        this.photoSources = sources;
    }

    public Order getOrderWithPhotos(List<String> sources) {
        return new Order(this, sources);
    }
}
