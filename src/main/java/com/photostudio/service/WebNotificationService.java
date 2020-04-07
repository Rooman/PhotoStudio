package com.photostudio.service;

import com.photostudio.entity.user.User;
import com.photostudio.service.entity.OrderIdAndMessageText;
import com.photostudio.service.entity.UserWSSession;

import javax.websocket.Session;

public interface WebNotificationService {
    void notification(User user, OrderIdAndMessageText orderIdAndMessageText);

    void addSession(UserWSSession session);

    void removeSession(Session session);
}
