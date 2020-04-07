package com.photostudio.service;

import com.photostudio.entity.user.User;
import com.photostudio.service.entity.UserWSSession;

import javax.websocket.Session;

public interface WebNotificationService {
    void notification(int orderId, User user, String message);

    void addSession(UserWSSession session);

    void removeSession(Session session);
}
