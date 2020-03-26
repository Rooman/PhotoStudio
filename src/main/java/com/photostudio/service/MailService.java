package com.photostudio.service;


import com.photostudio.entity.order.OrderStatus;
import com.photostudio.entity.user.User;

public interface MailService {
    void sendOnChangeStatus(User userChanged, int orderId, OrderStatus orderStatus);
}
