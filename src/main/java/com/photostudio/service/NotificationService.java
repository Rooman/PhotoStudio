package com.photostudio.service;


import com.photostudio.entity.order.OrderStatus;
import com.photostudio.entity.user.User;

public interface NotificationService {
    void sendOnChangeStatus(User userChanged, int orderId, OrderStatus orderStatus);

    void sendNewPassword(User user, String password);
}
