package com.photostudio.service;

import com.photostudio.entity.order.OrderStatus;
import com.photostudio.entity.user.User;
import com.photostudio.service.entity.UserWSSession;

import javax.websocket.Session;

public interface NotificationService {
    void notification(int orderId, User user, OrderStatus orderStatus);

    void addSession(UserWSSession session);

    void removeSession(Session session);
}
