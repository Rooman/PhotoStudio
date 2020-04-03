package com.photostudio.entity.order;

import com.photostudio.entity.photo.Photo;
import com.photostudio.entity.user.User;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@ToString
@Builder
@EqualsAndHashCode
public final class Order {
    private int id;
    private OrderStatus status;
    private LocalDateTime orderDate;
    private User user;
    private String commentAdmin;
    private String commentUser;
    private List<Photo> photoSources;
}
