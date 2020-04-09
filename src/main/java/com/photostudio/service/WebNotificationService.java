package com.photostudio.service;

import com.photostudio.entity.user.User;
import com.photostudio.service.entity.OrderIdAndMessageText;
import com.photostudio.service.entity.UserWSSession;

import javax.websocket.Session;

public interface WebNotificationService {
    void notificate(User user, Object objectMessage);

    void addSession(UserWSSession session);

    void removeSession(Session session);
}
