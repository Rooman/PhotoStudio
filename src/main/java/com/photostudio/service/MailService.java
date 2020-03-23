package com.photostudio.service;


import com.photostudio.entity.order.OrderStatus;
import com.photostudio.entity.user.User;

public interface MailService {

    void sendOnChangeStatus(User user, int orderId, OrderStatus orderStatus);
}
